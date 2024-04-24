package com.lab49.assignment.taptosnap.repository.labels

import com.lab49.assignment.taptosnap.dataStructures.ImageLabel
import com.lab49.assignment.taptosnap.network.BackendApi
import com.lab49.assignment.taptosnap.network.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class LabelsRepositoryImpl @Inject constructor(private val offlineSource: LabelsOfflineSource,
                                               private val api: BackendApi,
    private val networkHelper: NetworkHelper
) : LabelsRepository {
    private val onlineLabelsFlow = MutableStateFlow<Set<String>>(emptySet())

    override suspend fun fetchLabels(): Boolean {
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
            setLabels(labels)
            onlineLabelsFlow.emit(labels)
        }
    }

    // Return the saved labels if they exist, null otherwise
    override fun loadLabels(): Set<String>? {
        return offlineSource.getLabels()
    }

    override fun observeLabels(): Flow<Set<String>> {
       return onlineLabelsFlow
    }

    override fun setLabels(values: Set<String>?) {
        offlineSource.setLabels(values)
    }
}