package com.lab49.assignment.taptosnap.ui.game

import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.lab49.assignment.taptosnap.BuildConfig
import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.dataStructures.PictureRequest
import com.lab49.assignment.taptosnap.databinding.FragmentGameBinding
import com.lab49.assignment.taptosnap.ui.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class GameFragment(private val debugHelper: DebugHelper): Fragment(R.layout.fragment_game) {
    private lateinit var adapter: GameTaskAdapter
    private val splashModel by activityViewModels<SplashViewModel>()
    private val viewModel by viewModels<GameViewModel>()
    private val pictureResponseHandler = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            viewModel.imageSaved()
        } else {
            debugHelper.print("Failed to save image")
            viewModel.clearImageRequest()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGameBinding.bind(view)
        val recyclerView = binding.tasks
        recyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        val labels = splashModel.getOfflineLabels() ?: emptySet()
        adapter = GameTaskAdapter(lifecycleScope, labels)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                println("Repeat on lifecycle started")
                observeGameUpdates(this)
                observePictureRequests(this)
            }
        }

    }

    private fun observeGameUpdates(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            viewModel.gameUpdates
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onStart { debugHelper.print("listing to game updates") }
                .onCompletion { debugHelper.print("no longer listening to game updates") }
                .filterNotNull()
                .collect { update -> adapter.dataSetUpdated(update) }
        }
    }

    private fun observePictureRequests(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            adapter.clickedLabel
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onStart { debugHelper.print("listing to label clicks") }
                .onCompletion { debugHelper.print("no longer listening to label clicks") }
                .collect { selectedLabel ->
                    debugHelper.print("SelectedLabel : $selectedLabel")
                    takePicture(selectedLabel)
                }
        }
    }

    private fun takePicture(label: String) {
        val pictureStorage = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), label)
        val imageUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", pictureStorage);
        val pictureRequest = PictureRequest(label, imageUri)
        viewModel.pendingImage(pictureRequest)
        pictureResponseHandler.launch(imageUri)
    }

    companion object {
        private const val SPAN_COUNT = 2
    }
}