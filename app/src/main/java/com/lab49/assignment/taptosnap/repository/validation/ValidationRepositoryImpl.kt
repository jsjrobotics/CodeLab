package com.lab49.assignment.taptosnap.repository.validation

import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.dataStructures.ApiValidateResponse
import com.lab49.assignment.taptosnap.dataStructures.GameCardValidState
import com.lab49.assignment.taptosnap.dataStructures.ValidateRequest
import com.lab49.assignment.taptosnap.dataStructures.ValidateResponse
import com.lab49.assignment.taptosnap.network.BackendApi
import com.lab49.assignment.taptosnap.network.NetworkHelper
import javax.inject.Inject

class ValidationRepositoryImpl @Inject constructor(private val api: BackendApi,
                                                   private val networkHelper: NetworkHelper,
    private val debugHelper: DebugHelper
) : ValidationRepository {
    override suspend fun validateImage(validateRequest: ValidateRequest) {
        if (networkHelper.isOnline()) {
            debugHelper.print("validating ${validateRequest.imageLabel}")
            val response = api.asyncValidateImage(validateRequest)
            if (response != null && response.status == "SUCCESS") {
                debugHelper.print("${response.data.imageLabel} -> ${response.data.matched}")
            } else {
                debugHelper.print(" Failed to validate, error")
            }
        } else {
            debugHelper.print(" Failed to validate, offline")
        }
    }

}