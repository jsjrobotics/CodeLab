package com.lab49.assignment.taptosnap

import javax.inject.Inject

class DebugHelper @Inject constructor() {
    private val isDebug = true

    fun print(output: String) {
        if (isDebug) {
            println(output)
        }
    }
}
