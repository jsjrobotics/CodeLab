package com.lab49.assignment.taptosnap.ui.game

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class GameFragment(private val debugHelper: DebugHelper): Fragment(R.layout.fragment_game) {
    private lateinit var timer: TextView
    private lateinit var adapter: GameTaskAdapter
    private val splashModel by activityViewModels<SplashViewModel>()
    private val viewModel by activityViewModels<GameViewModel>()
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
        timer = binding.countdown
        val recyclerView = binding.tasks
        recyclerView.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        adapter = GameTaskAdapter(lifecycleScope)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                debugHelper.print("Repeat on lifecycle started")
                observeGameUpdates(this)
                observePictureRequests(this)
                observeTimerUpdates(this)
                observerGameCompletion(this)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (!viewModel.gameInProgress) {
            val labels = splashModel.getOfflineLabels() ?: run {
                debugHelper.print("Game is starting with no tasks")
                emptySet()
            }
            viewModel.setGameTasks(labels)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.beginGame()
    }

    private fun observeTimerUpdates(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            viewModel.timerUpdate
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { update -> timer.text = update }
        }
    }

    private fun observerGameCompletion(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            viewModel.gameCompletion
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .filterNotNull()
                .collect { gameWon ->
                    displayGameEndDialog(gameWon)
                }
        }
    }

    private fun displayGameEndDialog(gameWon: Boolean) {
        val message = if (gameWon) {
            R.string.game_won
        } else {
            R.string.better_luck
        }
        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_TapToSnap)
        builder.setTitle(R.string.game_over)
            .setMessage(message)
            .setPositiveButton(
                R.string.retry_permission
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                findNavController().navigate(R.id.action_gameFragment_to_splashFragment)
            }.setNegativeButton(R.string.exit) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.show()
    }

    private fun observeGameUpdates(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            viewModel.gameUpdates
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onStart { debugHelper.print("listing to game updates") }
                .onCompletion { debugHelper.print("no longer listening to game updates") }
                .filterNotNull()
                .collect { update ->
                    adapter.dataSetUpdated(update)
                }
        }
    }

    private fun observePictureRequests(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            adapter.clickedLabel
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onStart { debugHelper.print("listing to label clicks") }
                .onCompletion { debugHelper.print("no longer listening to label clicks") }
                .collect { selectedLabel ->
                    if (!viewModel.gameInProgress) {
                        return@collect
                    }
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