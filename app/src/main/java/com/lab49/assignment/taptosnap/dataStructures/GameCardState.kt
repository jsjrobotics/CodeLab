package com.lab49.assignment.taptosnap.dataStructures

import android.net.Uri

data class GameCardState(val label: String,
                         var validState: GameCardValidState? = null,
                         var validationState: GameCardValidationState? = null,
                         var imageUri: Uri? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameCardState

        if (label != other.label) return false
        if (validState != other.validState) return false
        if (validationState != other.validationState) return false
        return imageUri == other.imageUri
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + (validState?.hashCode() ?: 0)
        result = 31 * result + (validationState?.hashCode() ?: 0)
        result = 31 * result + (imageUri?.hashCode() ?: 0)
        return result
    }
}