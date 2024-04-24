package com.lab49.assignment.taptosnap.ui.splash

import androidx.lifecycle.ViewModel
import com.lab49.assignment.taptosnap.repository.labels.LabelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val labelsRepository: LabelsRepository
): ViewModel() {

    fun setLabels(values: Set<String>?) {
        labelsRepository.setLabels(values)
    }
    fun getOfflineLabels(): Set<String>? {
        return labelsRepository.loadLabels()
    }

    fun getOnlineLabels(): Boolean {
        return labelsRepository.fetchLabels()
    }
}