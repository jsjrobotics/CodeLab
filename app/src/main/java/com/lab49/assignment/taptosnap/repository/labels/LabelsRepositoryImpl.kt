package com.lab49.assignment.taptosnap.repository.labels

import com.lab49.assignment.taptosnap.BackendApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class LabelsRepositoryImpl @Inject constructor(private val offlineSource: LabelsOfflineSource,
                                               private val api: BackendApi
) : LabelsRepository {
    // Return true if a network call is being made, false otherwise.
    // Output is observed in the Flow from observeLabels
    override fun fetchLabels(): Boolean {
        return api.fetchLabels()
    }
    // Return the saved labels if the exit, null otherwise
    override fun loadLabels(): Set<String>? {
        return offlineSource.getLabels()
    }

    override fun observeLabels(): Flow<Set<String>> {
       return emptyFlow()
    }

    override fun setLabels(values: Set<String>?) {
        offlineSource.setLabels(values)
    }
}