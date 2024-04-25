package com.lab49.assignment.taptosnap.dataStructures

import android.net.Uri

data class GameCardState(val label: String,
                         var validState: GameCardValidState? = null,
                         var validationState: GameCardValidationState? = null,
                         var imageUri: Uri? = null)