package com.lab49.assignment.taptosnap.ui.splash

import androidx.lifecycle.ViewModel
import com.lab49.assignment.taptosnap.network.NetworkHelper
import com.lab49.assignment.taptosnap.repository.labels.LabelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val labelsRepository: LabelsRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val networkScope = CoroutineScope(Dispatchers.IO + viewModelJob)


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun setLabels(values: Set<String>?) {
        labelsRepository.setLabels(values)
    }
    fun getOfflineLabels(): Set<String>? {
        return labelsRepository.loadLabels()
    }

    fun getOnlineLabels(): Boolean {
        if (networkHelper.isOnline()) {
            networkScope.launch {
                labelsRepository.fetchLabels()
            }
            return true
        }
        return false
    }
}