package com.example.ble_project

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ble_project.ble.BleAdapter
import com.example.ble_project.ble.BleScanner
import com.example.ble_project.ble.ScanCallback
import com.example.ble_project.data.DataSource
import com.example.ble_project.utility.Permissions

private const val SCAN_PERIOD_MS: Long = 300000 //5 minutes

class MainActivityViewModel(activity: AppCompatActivity, val dataSource: DataSource) : ViewModel() {

    val beaconsLiveData = dataSource.getBeaconList()
    private var bleScanner: BleScanner? = null
    private var bleAdapter: BluetoothAdapter? = null
    private var permissionChecker = Permissions(activity)
    private val activityBleResultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
    var isScanningActive: MutableLiveData<Boolean>? = null

    init {
        //Get instance of Bluetooth Adapter
        bleAdapter = BleAdapter.get(activity)
        if (bleAdapter != null) {
            bleScanner = BleScanner(bleAdapter!!)
            bleScanner!!.setCallback(ScanCallback(dataSource))
        }
        isScanningActive = bleScanner?.scanning
    }

    //Swipe Down Action
    fun onRefreshAction() {
        dataSource.removeAllBeacons()
    }

    //Scanning Button Logic
    @RequiresApi(Build.VERSION_CODES.S)
    fun startButtonListener() {
        //Check if permissions are granted
        var isPermissionGranted = true
        if (!permissionChecker.checkRequiredBlePermissions())
        {
            isPermissionGranted = permissionChecker.requestRequiredBlePermissions()
        }
        if (isPermissionGranted) {
            //Check if BLE is enabled
            if (!bleAdapter!!.isEnabled) {
                promptEnableBluetooth()
            }
            else {
                bleScanner!!.delayScanning(SCAN_PERIOD_MS)
            }
        }
    }

    //Prompt user to enable bluetooth
    private fun promptEnableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activityBleResultLauncher.launch(enableBtIntent)
    }

}

class MainActivityViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(
                activity = activity,
                dataSource = DataSource.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

