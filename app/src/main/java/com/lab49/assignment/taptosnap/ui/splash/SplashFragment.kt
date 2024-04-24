package com.lab49.assignment.taptosnap.ui.splash

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lab49.assignment.taptosnap.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {
    val viewModel by viewModels<SplashViewModel>()

}