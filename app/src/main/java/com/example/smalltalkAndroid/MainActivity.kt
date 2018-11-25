package com.example.smalltalkAndroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.smalltalkAndroid.databinding.ActivityMainBinding
import com.example.smalltalkAndroid.feature.speech.SpeechFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.toolbar.title = "SmallTalk"
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        startFragment(SpeechFragment.newInstance(), false)
    }

    fun startFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).also {
            if (isAddToBackStack) {
                it.addToBackStack(null)
            }
        }.add(R.id.fragment_placeholder, fragment).commit()
    }
}