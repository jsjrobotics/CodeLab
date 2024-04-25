package com.lab49.assignment.taptosnap.repository.validation

import com.lab49.assignment.taptosnap.dataStructures.ValidateRequest

interface ValidationRepository {
    suspend fun validateImage(validateRequest: ValidateRequest)
}