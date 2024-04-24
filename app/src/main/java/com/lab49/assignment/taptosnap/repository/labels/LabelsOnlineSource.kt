package com.lab49.assignment.taptosnap.repository.labels

import com.lab49.assignment.taptosnap.dataStructures.ImageLabel
import com.lab49.assignment.taptosnap.network.BackendApi
import com.lab49.assignment.taptosnap.network.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LabelsOnlineSource @Inject constructor(private val api: BackendApi,
                                             private val networkHelper: NetworkHelper
) {
    private val _onlineLabelsFlow = MutableStateFlow<Set<String>>(emptySet())
    val onlineLabelsFlow = _onlineLabelsFlow.asStateFlow()
    suspend fun downloadLabels(): Boolean {
        if (networkHelper.isOnline()) {
            val response = api.asyncFetchLabels()
            if (response?.data != null) {
                processLabelResponse(response.data)
                return true
            }
            return false
        }
        return false
    }

    private suspend fun processLabelResponse(data: List<ImageLabel>?) {
        val receivedLabels = data?.map { it.imageLabel }
        receivedLabels?.let {
                val labels = it.toSet()
                _onlineLabelsFlow.emit(labels)
            }
    }

}