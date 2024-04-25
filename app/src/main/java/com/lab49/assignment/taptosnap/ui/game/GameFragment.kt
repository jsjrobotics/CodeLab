package com.lab49.assignment.taptosnap.ui.game

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.databinding.FragmentGameBinding
import com.lab49.assignment.taptosnap.ui.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment: Fragment(R.layout.fragment_game) {
    private val splashModel by activityViewModels<SplashViewModel>()
    val viewModel by viewModels<GameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGameBinding.bind(view)
        val recyclerView = binding.tasks
        recyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        val labels = splashModel.getOfflineLabels() ?: emptySet()
        val adapter = GameTaskAdapter(lifecycleScope, labels)
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.clickedLabel.filterNotNull().collect { selectedLabel ->
                    println("SelectedLabel : $selectedLabel")
                    viewModel.taskSelected(selectedLabel)
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    try {
                        requireActivity().startActivityForResult(takePictureIntent, 1)
                    } catch (e: ActivityNotFoundException) {
                        // display error state to the user
                    }

                }
            }
        }

    }

    companion object {
        private const val SPAN_COUNT = 2
    }
}