package com.lab49.assignment.taptosnap.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab49.assignment.taptosnap.network.NetworkHelper
import com.lab49.assignment.taptosnap.repository.labels.LabelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val labelsRepository: LabelsRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val networkScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val _labelsLoaded = MutableStateFlow<Boolean>(false)
    val labelsLoaded : StateFlow<Boolean> = _labelsLoaded

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun getOfflineLabels(): Set<String>? {
        return labelsRepository.loadLabels()
    }

    fun getOnlineLabels(): Boolean {
        if (networkHelper.isOnline()) {
            networkScope.launch {
                labelsRepository.fetchLabels()
                labelsRepository.observeLabels().collect { updatedLabels ->
                    println("Received labels: $updatedLabels")
                    _labelsLoaded.value = true
                }
            }
            return true
        }
        return false
    }
}