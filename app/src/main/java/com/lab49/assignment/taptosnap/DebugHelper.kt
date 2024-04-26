package com.lab49.assignment.taptosnap

import javax.inject.Inject

class DebugHelper @Inject constructor() {
    private val isDebug = true

    fun print(output: String, andWith: Boolean = true, orWith: Boolean = false) {
        if (orWith || (isDebug && andWith)) {
            println(output)
        }
    }
}
