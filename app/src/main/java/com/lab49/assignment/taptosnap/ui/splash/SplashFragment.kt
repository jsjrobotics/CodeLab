package com.lab49.assignment.taptosnap.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {
    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSplashBinding.bind(view)
        binding.buttonStart.setOnClickListener{
            viewModel.getOnlineLabels()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}