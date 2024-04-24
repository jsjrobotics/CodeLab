package com.lab49.assignment.taptosnap.repository.labels

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface LabelsRepository {
    // Return true if a network call is being made, false otherwise.
    // Output is observed in the Flow from observeLabels
    suspend fun fetchLabels(): Boolean

    fun setLabels(values: Set<String>?)
    fun observeLabels(): Flow<Set<String>>

    // Retrieve previously saved label values
    fun loadLabels(): Set<String>?
}