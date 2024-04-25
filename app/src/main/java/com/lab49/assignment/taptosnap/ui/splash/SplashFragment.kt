package com.lab49.assignment.taptosnap.ui.splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * The splash fragment subscribes to its view model to observe changes regarding whether labels
 * are loaded or not. If they are loaded, enable the Start button. If not loaded, disable Start button.
 */
@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {
    private val viewModel by activityViewModels<SplashViewModel>()
    private lateinit var buttonStart: AppCompatButton
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSplashBinding.bind(view)
        buttonStart = binding.buttonStart
        buttonStart.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_splashFragment_to_gameFragment)
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