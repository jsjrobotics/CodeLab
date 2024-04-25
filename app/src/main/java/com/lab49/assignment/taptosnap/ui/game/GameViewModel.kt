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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@HiltViewModel
class GameViewModel @Inject constructor(private val debug: DebugHelper): ViewModel()  {
    private var gameInProgress: Boolean = false
    private var pictureRequest: PictureRequest? = null
    private val gameState = mutableListOf<GameCardState>()
    private val _gameUpdates = MutableStateFlow<List<GameCardState>?>(null)
    private val _timerUpdate = MutableStateFlow<String?>(null)

    val timerUpdate = _timerUpdate.asStateFlow()
    val gameUpdates = _gameUpdates.asStateFlow()
    private var remainingSeconds = GAME_TIME_LIMIT_SECONDS

    private fun startTimer() {
        viewModelScope.launch {
            emitTime()
            oneSecondTimer().take(GAME_TIME_LIMIT_SECONDS)
                .onCompletion { gameInProgress = false }
                .onEach {
                    remainingSeconds -= 1
                    emitTime()
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }

    private suspend fun emitTime() {
        val minutesLeft = remainingSeconds / 60
        val secondsLeft = remainingSeconds % 60
        val display = String.format("%02d:%02d:%02d", 0, minutesLeft, secondsLeft)
        debug.print("Time left $display")
        _timerUpdate.emit(display)
    }

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

    @OptIn(ExperimentalTime::class)
    fun oneSecondTimer() = flow {
        while (true) {
            emit(Unit)
            delay(1.seconds)
        }
    }

    fun beginGame() {
        if (!gameInProgress) {
            gameInProgress = true
            startTimer()
        }
    }

    companion object {
        private const val GAME_TIME_LIMIT_SECONDS = 120
    }
}