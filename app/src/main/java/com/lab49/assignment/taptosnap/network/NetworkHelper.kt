package com.lab49.assignment.taptosnap.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHelper @Inject constructor() {
    fun isOnline(): Boolean {
        return true
    }
}
