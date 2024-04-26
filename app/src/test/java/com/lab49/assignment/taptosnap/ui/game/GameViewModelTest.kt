package com.lab49.assignment.taptosnap.ui.game

import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.MainDispatcherRule
import com.lab49.assignment.taptosnap.repository.validation.ValidationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private lateinit var mockValidationRepository: ValidationRepository
    private lateinit var mockDebugHelper: DebugHelper
    private lateinit var testSubject: GameViewModel
    private val scheduler = TestCoroutineScheduler()
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(scheduler)

    @Before
    fun setUp() {
        mockValidationRepository = Mockito.mock(ValidationRepository::class.java)
        mockDebugHelper = Mockito.mock(DebugHelper::class.java)
        testSubject = GameViewModel(mockDebugHelper, mockValidationRepository)
    }

    @Test
    fun getGameInProgress() = runTest {
        launch {
            assert(!testSubject.gameInProgress) { "Game should not be inProgress until beginGame()" }
            testSubject.beginGame()
            scheduler.advanceTimeBy(1L)

            assert(testSubject.gameInProgress) { "Game should be inProgress after game Start" }
            scheduler.advanceTimeBy(GameViewModel.Companion.GAME_TIME_LIMIT_SECONDS.toLong())
            val list = testSubject.gameCompletion.toList()
            assert(list.size == 1) { "Game should terminate after GAME_TIME_LIMIT_SECONDS seconds" }
        }

    }

    @Test
    fun getGameCompletion() {
    }

    @Test
    fun getTimerUpdate() {
    }

    @Test
    fun getGameUpdates() {
    }

    @Test
    fun onCleared() {
    }

    @Test
    fun imageSaved() {
    }

    @Test
    fun validateImage() {
    }

    @Test
    fun pendingImage() {
    }

    @Test
    fun clearImageRequest() {
    }

    @Test
    fun setGameTasks() {
    }

    @Test
    fun beginGame() {
    }

    @Test
    fun currentState() {
    }
}