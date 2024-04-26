package com.lab49.assignment.taptosnap.ui.game

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.dataStructures.GameCardState
import com.lab49.assignment.taptosnap.dataStructures.GameCardValidState
import com.lab49.assignment.taptosnap.dataStructures.GameCardValidationState
import com.lab49.assignment.taptosnap.dataStructures.PictureRequest
import com.lab49.assignment.taptosnap.dataStructures.ValidateRequest
import com.lab49.assignment.taptosnap.dataStructures.ValidateResponse
import com.lab49.assignment.taptosnap.repository.validation.ValidationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@HiltViewModel
class GameViewModel @Inject constructor(private val debugHelper: DebugHelper,
                                        private val validationRepository: ValidationRepository
): ViewModel()  {
    private var timerJob: Job? = null
    private var awaitingValidation: String? = null
    var gameInProgress: Boolean = false ; private set
    private val viewModelJob = SupervisorJob()
    private val networkScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var pictureRequest: PictureRequest? = null
    private val gameState = mutableListOf<GameCardState>()
    private val _gameUpdates = MutableSharedFlow<GameCardState?>()
    private val _timerUpdate = MutableStateFlow<String?>(null)
    private val _gameCompletion = MutableStateFlow<Boolean?>(null)
    val gameCompletion = _gameCompletion.asStateFlow()
    val timerUpdate = _timerUpdate.asStateFlow()
    val gameUpdates = _gameUpdates.asSharedFlow()
    private var remainingSeconds = GAME_TIME_LIMIT_SECONDS


    override fun onCleared() {
        debugHelper.print("View model jobs cleared")
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            remainingSeconds = GAME_TIME_LIMIT_SECONDS
            awaitingValidation = null
            gameInProgress = true
            _gameCompletion.emit(null)
            emitTime()
            oneSecondTimer().take(GAME_TIME_LIMIT_SECONDS)
                .flowOn(Dispatchers.IO)
                .collect {
                    remainingSeconds -= 1
                    emitTime()
                    if (remainingSeconds <= 0) {
                        endGame()
                    }
                }
        }

        viewModelScope.launch {
            validationRepository.observeResponses()
                .filterNotNull()
                .collect { validateResponse ->
                    updateTaskValidated(validateResponse)
                }
        }
    }

    private suspend fun endGame() {
        gameInProgress = false
        _gameCompletion.emit(hasGameBeenWon())
        timerJob?.cancel()
        timerJob = null
    }

    private fun hasGameBeenWon(): Boolean {
        return gameState.all { it.validState == GameCardValidState.VALID }
    }

    private suspend fun emitTime() {
        val minutesLeft = remainingSeconds / 60
        val secondsLeft = remainingSeconds % 60
        val display = String.format("%02d:%02d:%02d", 0, minutesLeft, secondsLeft)
        debugHelper.print("Time left $display", andWith = false)
        _timerUpdate.emit(display)
    }

    private fun buildDataList(labels: Set<String>): MutableList<GameCardState> {
        return labels.map { GameCardState(it) }.toMutableList()
    }

    fun imageSaved() {
        val label = pictureRequest?.label ?: return
        val uri = pictureRequest?.imageUri ?: return
        debugHelper.print("$label Picture saved at $uri")
        updateTaskToValidating(label, uri)
        awaitingValidation = label
    }

    private fun updateTaskToValidating(label: String, uri: Uri) {
        val updatedState = gameState.find { it.label == label }
        if (updatedState == null || !gameInProgress ) {
            return
        }
        updatedState.let {
            it.imageUri = uri
            it.validationState = GameCardValidationState.VALIDATING
            debugHelper.print("Validating update")
            notifyGameUpdate(it)
        }
    }

    private fun updateTaskValidated(validateResponse: ValidateResponse) {
        val updatedState = gameState.find { it.label == validateResponse.imageLabel }
        if (updatedState == null || !gameInProgress ) {
            return
        }
        updatedState.let {
            it.validationState = GameCardValidationState.NOT_VALIDATING
            val state = if (validateResponse.matched) {
                GameCardValidState.VALID
            } else {
                GameCardValidState.INVALID
            }
            it.validState = state
            debugHelper.print("Validated update")
            notifyGameUpdate(it)
            if (gameState.all { checkState -> checkState.validState == GameCardValidState.VALID }) {
                viewModelScope.launch { endGame() }
            }
        }
    }

    private fun notifyGameUpdate(gameCardState: GameCardState) {
        viewModelScope.launch{
            debugHelper.print("Posting Game update: ${gameCardState.hashCode()}")
            _gameUpdates.emit(gameCardState)
        }
    }

    fun validateImage(validateRequest: ValidateRequest) {
        if (validateRequest.imageLabel == awaitingValidation) {
            awaitingValidation = null
            networkScope.launch {
                validationRepository.validateImage(validateRequest)
            }
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
        buildDataList(labels).forEach {
            gameState.add(it)
            viewModelScope.launch {
                notifyGameUpdate(it)
            }
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
            debugHelper.print("Game Starting")
            startTimer()
        }
    }

    fun currentState(): List<GameCardState> {
        return gameState.toList()
    }

    companion object {
        private const val GAME_TIME_LIMIT_SECONDS = 120
    }
}