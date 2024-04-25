package com.lab49.assignment.taptosnap.ui.game

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.dataStructures.GameCardState
import com.lab49.assignment.taptosnap.dataStructures.PictureRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val debug: DebugHelper): ViewModel()  {
    private var pictureRequest: PictureRequest? = null
    private val gameState = mutableListOf<GameCardState>()
    private val _gameUpdates = MutableStateFlow<List<GameCardState>?>(null)
    val gameUpdates = _gameUpdates.asStateFlow()

    private fun buildDataList(labels: Set<String>): MutableList<GameCardState> {
        return labels.map { GameCardState(it) }.toMutableList()
    }

    fun imageSaved() {
        val label = pictureRequest?.label ?: return
        val uri = pictureRequest?.imageUri ?: return
        debug.print("$label Picture saved at $uri")
        gameState.find { it.label == label }?.let { it.imageUri = uri }
        viewModelScope.launch{
           _gameUpdates.emit(gameState)
        }
    }

    fun pendingImage(pictureRequest: PictureRequest) {
        this.pictureRequest = pictureRequest
    }

    fun clearImageRequest() {
        pictureRequest = null
    }

    fun setGameTasks(labels: Set<String>) {
        gameState.clear()
        buildDataList(labels).forEach { gameState.add(it) }
        viewModelScope.launch{
            _gameUpdates.emit(gameState)
        }
    }
}