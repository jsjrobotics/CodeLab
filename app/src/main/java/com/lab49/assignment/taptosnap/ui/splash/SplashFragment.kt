package com.lab49.assignment.taptosnap.ui.splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {
    private val viewModel by viewModels<SplashViewModel>()
    private lateinit var buttonStart: AppCompatButton
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSplashBinding.bind(view)
        buttonStart = binding.buttonStart
        buttonStart.setOnClickListener{
            // Navigate away
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.labelsLoaded.collect { isLabelsLoaded ->
                    println("Labels Loaded: $isLabelsLoaded")
                    if (!isLabelsLoaded) {
                        viewModel.getOnlineLabels()
                    }
                    buttonStart.isClickable = isLabelsLoaded

                }
            }
        }
    }
}