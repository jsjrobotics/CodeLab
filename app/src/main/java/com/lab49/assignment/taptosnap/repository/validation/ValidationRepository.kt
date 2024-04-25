package com.lab49.assignment.taptosnap.repository.validation

import com.lab49.assignment.taptosnap.dataStructures.ValidateRequest
import com.lab49.assignment.taptosnap.dataStructures.ValidateResponse
import kotlinx.coroutines.flow.Flow

interface ValidationRepository {
    suspend fun validateImage(validateRequest: ValidateRequest)
    fun observeResponses(): Flow<ValidateResponse?>

}