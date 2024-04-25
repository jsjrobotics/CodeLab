package com.lab49.assignment.taptosnap.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * The splash fragment subscribes to its view model to observe changes regarding whether labels
 * are loaded or not. If they are loaded, enable the Start button. If not loaded, disable Start button.
 */
@AndroidEntryPoint
class SplashFragment(private val debugHelper: DebugHelper): Fragment(R.layout.fragment_splash) {
    private val viewModel by activityViewModels<SplashViewModel>()
    private lateinit var buttonStart: AppCompatButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState == null) {
            viewModel.clearOfflineLabels()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSplashBinding.bind(view)
        buttonStart = binding.buttonStart
        buttonStart.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_splashFragment_to_gameFragment)
            buttonStart.isClickable = false
        }
        viewModel.labelsLoaded.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { isLabelsLoaded ->
                debugHelper.print("Labels Loaded: $isLabelsLoaded")
                if (!isLabelsLoaded) {
                    viewModel.getOnlineLabels()
                }
                buttonStart.isClickable = isLabelsLoaded
            }.launchIn(lifecycleScope)
    }
}