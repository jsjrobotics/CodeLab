package com.lab49.assignment.taptosnap.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        //navController.graph = navController.createGraph(startDestination = "splash") {
          //  fragment<GameFragment>("game") { label = "game" }
           // fragment<SplashFragment>("splash") { label = "splash" }
       // }
        /*val host = NavHostFragment.create(R.navigation.app_nav)
        supportFragmentManager.beginTransaction().replace(R.id.container, host).setPrimaryNavigationFragment(host).commit()

        if (savedInstanceState == null) {
            splashFragment = SplashFragment()
            gameFragment = GameFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.content, splashFragment)
                .add(R.id.content, gameFragment)
                .hide(splashFragment)
                .commit()
        }*/

    }
}