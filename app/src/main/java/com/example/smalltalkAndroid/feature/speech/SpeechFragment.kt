package com.example.smalltalkAndroid.feature.speech

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smalltalkAndroid.R
import com.example.smalltalkAndroid.databinding.FrSpeechBinding
import com.example.smalltalkAndroid.feature.ItemSpacer
import com.example.smalltalkAndroid.feature.cloud_messaging.SmalltalkMessagingService
import com.example.smalltalkAndroid.model.LocationParams
import com.example.smalltalkAndroid.utils.hideAlpha
import com.example.smalltalkAndroid.utils.showAlpha
import com.example.smalltalkAndroid.utils.shuffleAnimate
import com.github.ajalt.reprint.core.AuthenticationFailureReason
import com.github.ajalt.reprint.core.AuthenticationListener
import com.github.ajalt.reprint.core.Reprint
import com.google.android.material.snackbar.Snackbar
import com.mapzen.speakerbox.Speakerbox
import com.mcxiaoke.koi.ext.delayed
import com.mcxiaoke.koi.ext.onClick
import com.mcxiaoke.koi.ext.onTextChange
import com.mcxiaoke.koi.ext.onTouchEvent
import com.tbruyelle.rxpermissions2.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SpeechFragment : Fragment() {

    companion object {
        fun newInstance() = SpeechFragment()
    }

    private lateinit var binding: FrSpeechBinding
    private val viewModel: SpeechViewModel by viewModel()
    private var speechRecognizer: SpeechRecognizer? = null
    private var isRecognizerRunning = false
    private val conversationAdapter by lazy { ConversationAdapter() }
    private lateinit var speakerBox: Speakerbox
    private val ttsCallback = { text: String ->
        if (text.contains("\\uD"))
            speakerBox.play(text.substringBefore("\\u"))
        else
            speakerBox.play(text)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_speech, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.application?.let { speakerBox = Speakerbox(it) }
        @Suppress("DEPRECATION")
        RxPermissions(this).request(
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.USE_FINGERPRINT
        ).subscribe { granted ->
            if (granted) {
                setup()
                observe()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun callOperator() {
        RxPermissions(this).request(Manifest.permission.CALL_PHONE).subscribe { granted ->
            if (granted) {
                startActivity(Intent(Intent.ACTION_CALL).apply { data = Uri.parse("tel:+40720660316") })
            }
        }
    }

    private fun observe() {
        viewModel.receivedMessageObservable.observe(this, Observer {
            addMessageToList(it.response, MessageOwner.SERVER)
            if (it.requireValidation) {
                Handler().postDelayed({ startAuthentication() }, (it.response.length + 4) * 50L)
            }
            if (it.requireCall) {
                Handler().postDelayed({ callOperator() }, (it.response.length + 4) * 50L)
            }
            if (it.locationData != LocationParams()) {
                val uri =
                    "geo:${it.locationData.longitude},${it.locationData.latitude}?q=${it.locationData.longitude},${it.locationData.latitude}(${it.locationData.name})"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                Handler().postDelayed({ context?.startActivity(intent) }, (it.response.length + 4) * 50L)
            }
            if (it.url.isNotEmpty()) {
                Handler().postDelayed({
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.setPackage("com.android.chrome")
                    startActivity(intent)
                }, (it.response.length + 4) * 50L)
            }
        })
    }


    private fun doAnimation(reverse: Boolean) = if (!reverse) {
        binding.frSpeechBtnStartRecording.isEnabled = false
        binding.frSpeechBubble.resumeAnimation()
    } else {
        binding.frSpeechBtnStartRecording.isEnabled = true
        binding.frSpeechBubble.pauseAnimation()
        resetAnimation()
    }

    private fun resetAnimation() = binding.frSpeechBubble.run {
        ObjectAnimator.ofFloat(this, "progress", this.progress, 0f).setDuration(700).start()
    }

    private fun setup() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context ?: return)
        speechRecognizer?.setRecognitionListener(recognitionListener)
        binding.frSpeechBtnStartRecording.onClick {
            doAnimation(false)
            startVoiceRecognition()
        }
        binding.frSpeechRw.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.frSpeechRw.adapter =
                conversationAdapter.apply { textToSpeechCallback = this@SpeechFragment.ttsCallback }
        binding.frSpeechRw.addItemDecoration(ItemSpacer(context ?: return, R.dimen.msg_card_spacing))
        binding.frSpeechRw.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
            override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
                return EdgeEffect(view.context).apply {
                    color = ContextCompat.getColor(context ?: return@apply, R.color.appColor)
                }
            }
        }
        binding.frSpeechEt.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send, 0)
            onTouchEvent { _, motionEvent ->
                if (motionEvent.action == ACTION_UP && motionEvent.rawX >= (this.right - this.compoundPaddingRight)) {
                    if (this.text.isNotEmpty()) {
                        addMessageToList(this.text.toString(), MessageOwner.CLIENT)
                        viewModel.getResponse(this.text.toString())
                        setText("")
                    }
                    return@onTouchEvent true
                }
                false
            }
            onTextChange { charSequence, _, _, _ ->
                this.isCursorVisible = charSequence.isNotEmpty()
            }
        }
        Handler().postDelayed({
            addMessageToList(getString(R.string.greeting_message), MessageOwner.SERVER)
        }, 500)
    }

    private fun startAuthentication() {
        binding.frSpeechRw.hideAlpha(500)
        binding.frSpeechBtnStartRecording.hideAlpha(500)
        binding.frSpeechBubble.hideAlpha(500)
        binding.frSpeechEt.hideAlpha(500)
        Handler().postDelayed({
            binding.frSpeechAuthAnimation.showAlpha(500)
            binding.frSpeechAuthAnimation.playAnimation()
            Handler().postDelayed({ binding.frSpeechAuthAnimation.pauseAnimation() }, 800)
            Reprint.authenticate(object : AuthenticationListener {
                override fun onSuccess(moduleTag: Int) {
                    binding.frSpeechAuthAnimation.resumeAnimation()
                    Handler().postDelayed({
                        binding.frSpeechAuthAnimation.hideAlpha(500)
                        binding.frSpeechRw.showAlpha(500)
                        binding.frSpeechBtnStartRecording.showAlpha(500)
                        binding.frSpeechBubble.showAlpha(500)
                        binding.frSpeechEt.showAlpha(500)
                        Snackbar.make(binding.root, R.string.action_authorized, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.ok)) {}
                            .show()
                    }, 2500)
                    addMessageToList(getString(R.string.auth_confirmed), MessageOwner.SERVER)
                }

                override fun onFailure(
                    failureReason: AuthenticationFailureReason?,
                    fatal: Boolean,
                    errorMessage: CharSequence?,
                    moduleTag: Int,
                    errorCode: Int
                ) {
                    binding.frSpeechAuthAnimation.shuffleAnimate()
                    Snackbar.make(binding.root, R.string.action_revoked, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.close)) {
                            binding.frSpeechAuthAnimation.hideAlpha(500)
                            binding.frSpeechRw.showAlpha(500)
                            binding.frSpeechBtnStartRecording.showAlpha(500)
                            binding.frSpeechBubble.showAlpha(500)
                            binding.frSpeechEt.showAlpha(500)
                            addMessageToList(getString(R.string.auth_revoked), MessageOwner.SERVER)
                        }
                        .show()
                }
            })
        }, 550)
    }

    private fun startVoiceRecognition() {
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity?.packageName)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000L)
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000L)
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000L)
        speechIntent.putExtra("android.speech.extra.DICTATION_MODE", false)
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        isRecognizerRunning = true
        speechRecognizer?.startListening(speechIntent)
    }

    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onPartialResults(partialResults: Bundle?) {}

        override fun onEvent(eventType: Int, params: Bundle?) {}

        override fun onBeginningOfSpeech() {}

        override fun onEndOfSpeech() {}

        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                SpeechRecognizer.ERROR_CLIENT -> "Client error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission error"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Timeout error"
                SpeechRecognizer.ERROR_NO_MATCH -> "No match error"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy error"
                SpeechRecognizer.ERROR_SERVER -> "Server error"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout error"
                else -> "Error"
            }
            isRecognizerRunning = false
            doAnimation(true)
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.toList() ?: emptyList()
            isRecognizerRunning = false
            addMessageToList(matches.first(), MessageOwner.CLIENT)
            viewModel.getResponse(matches.first())
            doAnimation(true)
        }
    }

    private fun addMessageToList(message: String, owner: MessageOwner) {
        conversationAdapter.addMessage(message, owner)
        binding.frSpeechRw.smoothScrollToPosition(binding.frSpeechRw.adapter?.itemCount ?: 0)
        Handler().delayed((message.length + 4) * 50L) {
            binding.frSpeechRw.smoothScrollToPosition(binding.frSpeechRw.adapter?.itemCount ?: 0)
        }
    }
}