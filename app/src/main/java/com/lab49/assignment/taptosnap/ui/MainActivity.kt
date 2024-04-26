package com.lab49.assignment.taptosnap.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.lab49.assignment.taptosnap.DebugHelper
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.di.TapToSnapFragmentFactory
import com.lab49.assignment.taptosnap.ui.game.GameFragment
import com.lab49.assignment.taptosnap.ui.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val requestCode = hashCode()
    //TODO this should be injected
    val factory  = TapToSnapFragmentFactory(DebugHelper())
    @Inject
    lateinit var debugHelper: DebugHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = factory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        checkPermissions()

    }

    private fun checkPermissions() {
        val permissionsRequired = listOf(Manifest.permission.CAMERA)
        val requestPermissions = permissionsRequired.filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        if (requestPermissions.isNotEmpty()) {
            val permissionArray = requestPermissions.toTypedArray()
            ActivityCompat.requestPermissions(this, permissionArray, requestCode);
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach { grantResult ->
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                showPermissionRequired()
            }
        }
    }

    private fun showPermissionRequired() {
        val builder = AlertDialog.Builder(this, R.style.Theme_TapToSnap)
        builder.setTitle(R.string.permission_denied)
            .setMessage(R.string.requested_permissions_required)
            .setPositiveButton(R.string.retry_permission
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                checkPermissions()
            }.show()
    }
}