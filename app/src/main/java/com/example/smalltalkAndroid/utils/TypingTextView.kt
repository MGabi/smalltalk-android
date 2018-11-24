package com.example.smalltalkAndroid.utils

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


class TypingTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {

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
}