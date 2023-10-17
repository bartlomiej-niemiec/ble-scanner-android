package com.example.ble_project.utility

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


private const val permissionGrantedMsg: String = "Permission Granted"
private const val permissionNotGrantedMsg: String = "Permission not Granted"

/* Class provides checks and prompt for required Bluetooth permission for application. */
class Permissions(private val activity: AppCompatActivity) {


    private val requiredPermissions = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestSinglePermissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(activity, permissionGrantedMsg, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, permissionNotGrantedMsg, Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermisssion(permission: String): Int {
        return ActivityCompat.checkSelfPermission(activity, permission)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun checkRequiredBlePermissions() : Boolean {
        for (permission in this.requiredPermissions ){
            if (checkPermisssion(permission) != PackageManager.PERMISSION_GRANTED)
            {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun requestRequiredBlePermissions(): Boolean {

        for (permission in this.requiredPermissions ){
            requestSinglePermissionLauncher.launch(
                permission
            )
        }

        return checkRequiredBlePermissions()
    }

}