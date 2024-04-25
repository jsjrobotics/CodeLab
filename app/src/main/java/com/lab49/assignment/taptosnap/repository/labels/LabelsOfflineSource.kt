package com.lab49.assignment.taptosnap.repository.labels

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * A helper class to access shared preferences file that stores label values offline
 */
class LabelsOfflineSource @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences = context.getSharedPreferences(labelsFileName, 0)

    fun getLabels(): Set<String>? {
        return sharedPreferences.getStringSet(keyLabels, null)
    }

    fun setLabels(labelValues: Set<String>?): Boolean {
        val editor = sharedPreferences.edit()
        if (labelValues.isNullOrEmpty()) {
            editor.remove(keyLabels)
        } else {
            editor.putStringSet(keyLabels, labelValues)
        }
        return editor.commit()
    }

    companion object {
        const val labelsFileName = "shared_pref_labels"
        const val keyLabels = "labels"
    }
}