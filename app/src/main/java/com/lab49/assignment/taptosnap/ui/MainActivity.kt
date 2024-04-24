package com.lab49.assignment.taptosnap.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.ui.game.GameFragment
import com.lab49.assignment.taptosnap.ui.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var gameFragment: GameFragment
    private lateinit var splashFragment: SplashFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            splashFragment = SplashFragment()
            gameFragment = GameFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.content, splashFragment)
                .add(R.id.content, gameFragment)
                .hide(gameFragment)
                .commit()
        }

    }
}