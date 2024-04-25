package com.lab49.assignment.taptosnap.repository.validation

import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.dataStructures.ValidateRequest
import com.lab49.assignment.taptosnap.dataStructures.ValidateResponse
import com.lab49.assignment.taptosnap.network.BackendApi
import com.lab49.assignment.taptosnap.network.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ValidationRepositoryImpl @Inject constructor(private val api: BackendApi,
                                                   private val networkHelper: NetworkHelper,
    private val debugHelper: DebugHelper
) : ValidationRepository {
    private val _validateResponse = MutableStateFlow<ValidateResponse?>(null)
    val validateResponse = _validateResponse.asStateFlow()

    override suspend fun validateImage(validateRequest: ValidateRequest) {
        if (networkHelper.isOnline()) {
            debugHelper.print("validating ${validateRequest.imageLabel}")
            val response = api.asyncValidateImage(validateRequest)
            if (response != null && response.status == "SUCCESS") {
                debugHelper.print("${response.data.imageLabel} -> ${response.data.matched}")
                _validateResponse.emit(response.data)
            } else {
                _validateResponse.emit(ValidateResponse(validateRequest.imageLabel, false))
            }
        } else {
            _validateResponse.emit(ValidateResponse(validateRequest.imageLabel, false))
        }
    }

    override fun observeResponses(): Flow<ValidateResponse?> {
        return validateResponse
    }

}