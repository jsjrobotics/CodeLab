package com.lab49.assignment.taptosnap.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.dataStructures.GameCardState
import com.lab49.assignment.taptosnap.dataStructures.PictureRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val debug: DebugHelper): ViewModel()  {
    private var pictureRequest: PictureRequest? = null
    private val _gameUpdates = MutableSharedFlow<GameCardState>()
    val gameUpdates = _gameUpdates.asSharedFlow()

    fun imageSaved() : GameCardState? {
        val label = pictureRequest?.label ?: return null
        val uri = pictureRequest?.imageUri ?: return null
        debug.print("$label Picture saved at $uri")
        val updatedState = GameCardState(label, imageUri = uri)
        return updatedState
    }

    fun pendingImage(pictureRequest: PictureRequest) {
        this.pictureRequest = pictureRequest
    }

    fun clearImageRequest() {
        pictureRequest = null
    }
}