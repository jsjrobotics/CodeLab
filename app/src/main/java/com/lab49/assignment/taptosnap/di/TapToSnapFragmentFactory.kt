package com.lab49.assignment.taptosnap.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.ui.game.GameFragment
import com.lab49.assignment.taptosnap.ui.splash.SplashFragment
import javax.inject.Inject

class TapToSnapFragmentFactory @Inject constructor(private val debugHelper: DebugHelper) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        if (className == GameFragment::class.java.name) {
            return GameFragment(debugHelper)
        }
        if (className == SplashFragment::class.java.name) {
            return SplashFragment(debugHelper)
        }
        return super.instantiate(classLoader, className)
    }
}