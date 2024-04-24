package com.lab49.assignment.taptosnap.ui.game

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lab49.assignment.taptosnap.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment: Fragment(R.layout.fragment_game) {
    val viewModel by viewModels<GameViewModel>()

}