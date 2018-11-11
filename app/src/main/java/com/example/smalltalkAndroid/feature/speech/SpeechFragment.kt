package com.example.smalltalkAndroid.feature.speech

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smalltalkAndroid.R
import com.example.smalltalkAndroid.databinding.FrSpeechBinding
import com.example.smalltalkAndroid.disable
import com.example.smalltalkAndroid.enable
import com.example.smalltalkAndroid.feature.ItemSpacer
import com.google.android.material.snackbar.Snackbar
import com.mcxiaoke.koi.ext.onClick
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_speech, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RxPermissions(this).request(
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO
        )
            .subscribe { granted ->
                if (granted) {
                    setup()
                    observe()
                }
            }
    }

    @SuppressLint("CheckResult")
    fun callOperator() {
        RxPermissions(this).request(Manifest.permission.CALL_PHONE).subscribe { granted ->
            if (granted) {
                startActivity(Intent(Intent.ACTION_CALL).apply { data = Uri.parse("tel:+40720660316") })
            }
        }
    }

    private fun observe() {
        viewModel.receivedMessageObservable.observe(this, Observer {
            addMessageToList(it.text, it.owner)
        })
    }

    private fun setup() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context ?: return)
        speechRecognizer?.setRecognitionListener(recognitionListener)
        binding.frSpeechSkLoader.onClick {
            it.disable()
            startVoiceRecognition()
        }
        binding.frSpeechRw.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.frSpeechRw.adapter = conversationAdapter
        binding.frSpeechRw.addItemDecoration(ItemSpacer(context ?: return, R.dimen.msg_card_spacing))
        Handler().postDelayed({
            addMessageToList(getString(R.string.greeting_message), MessageOwner.SERVER)
        }, 500)
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
        override fun onReadyForSpeech(params: Bundle?) {

        }

        override fun onRmsChanged(rmsdB: Float) {

        }

        override fun onBufferReceived(buffer: ByteArray?) {

        }

        override fun onPartialResults(partialResults: Bundle?) {

        }

        override fun onEvent(eventType: Int, params: Bundle?) {

        }

        override fun onBeginningOfSpeech() {

        }

        override fun onEndOfSpeech() {

        }

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
            binding.frSpeechSkLoader.enable()
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.toList() ?: listOf()
            isRecognizerRunning = false
            addMessageToList(matches.first(), MessageOwner.CLIENT)
            viewModel.getResponse(matches.first())
            binding.frSpeechSkLoader.enable()
        }
    }

    fun addMessageToList(message: String, owner: MessageOwner) {
        conversationAdapter.addMessage(message, owner)
        binding.frSpeechRw.smoothScrollToPosition(binding.frSpeechRw.adapter?.itemCount ?: 0)
    }
}