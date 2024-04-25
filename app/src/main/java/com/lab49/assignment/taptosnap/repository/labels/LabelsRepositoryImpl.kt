package com.lab49.assignment.taptosnap.repository.labels

import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.dataStructures.ImageLabel
import com.lab49.assignment.taptosnap.network.BackendApi
import com.lab49.assignment.taptosnap.network.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class LabelsRepositoryImpl @Inject constructor(private val offlineSource: LabelsOfflineSource,
                                               private val onlineSource: LabelsOnlineSource,
    private val debug: DebugHelper
    ) : LabelsRepository {

    override suspend fun fetchLabels(): Boolean {
        val updated = onlineSource.downloadLabels()
        if (updated) {
            observeLabels().take(1).collect (::setLabels)
        }
        return updated
    }

    // Return the saved labels if they exist, null otherwise
    override fun loadLabels(): Set<String>? {
        return offlineSource.getLabels()
    }

    override fun observeLabels(): Flow<Set<String>> {
       return onlineSource.onlineLabelsFlow
    }

    override fun setLabels(values: Set<String>?) {
        debug.print("Saving labels $values")
        offlineSource.setLabels(values)
    }
}