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
        return (checkPermisssion(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED)
                && (checkPermisssion(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun requestRequiredBlePermissions(): Boolean {

        requestSinglePermissionLauncher.launch(
            Manifest.permission.BLUETOOTH_SCAN
        )
        requestSinglePermissionLauncher.launch(
            Manifest.permission.BLUETOOTH_CONNECT
        )
        return checkRequiredBlePermissions()
    }

}