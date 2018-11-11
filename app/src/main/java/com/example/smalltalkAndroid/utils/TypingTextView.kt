package com.example.smalltalkAndroid.utils

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.TextView


class TypingTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    TextView(context, attrs) {

    private var textToBeAnimated: CharSequence = ""
    private var mIndex: Int = 0
    var characterDelay: Long = 50 //Default 500ms delay

    private val mHandler = Handler()
    private val characterAdder = object : Runnable {
        override fun run() {
            text = textToBeAnimated.subSequence(0, mIndex++)
            if (mIndex <= textToBeAnimated.length) {
                mHandler.postDelayed(this, characterDelay)
            }
        }
    }

    var animateText: CharSequence
        get() = text
        set(value) {
            textToBeAnimated = value
            mIndex = 0
            text = ""
            mHandler.removeCallbacks(characterAdder)
            mHandler.postDelayed(characterAdder, characterDelay)
        }

    var animateTextContinuous: CharSequence
        get() = text
        set(value) {
            if (value.isNotEmpty()) {
                mIndex = if (textToBeAnimated.isEmpty()) 0 else textToBeAnimated.length - 1
                textToBeAnimated = value
                mHandler.removeCallbacks(characterAdder)
                mHandler.postDelayed(characterAdder, characterDelay)
            }
        }
}