package com.example.smalltalkAndroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.smalltalkAndroid.databinding.ActivityMainBinding
import com.example.smalltalkAndroid.feature.speech.SpeechFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.toolbar.title = "SmallTalk"
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_placeholder, SpeechFragment.newInstance()).commit()
    }
}