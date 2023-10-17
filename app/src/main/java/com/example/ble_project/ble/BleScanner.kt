package com.example.ble_project.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData


class BleScanner(bleAdapter: BluetoothAdapter) {

    private val bleCheckTaskInterval: Long = 1000 //ms
    private var bleScanner: BluetoothLeScanner? = null
    private var callBack : ScanCallback? = null
    private val handler = Handler(Looper.getMainLooper())
    private var delayScannRun: Runnable = object : Runnable {
        override fun run() {
            stopScanning()
        }
    }
    val scanning: MutableLiveData<Boolean> = MutableLiveData()
    companion object{
        val DEFAULT_SCAN_PERIOD: Long = 10000L // default Scan Perion
    }

    init {
        scanning.value = false
        this.bleScanner = bleAdapter.bluetoothLeScanner
        handler.post(object: Runnable {
            override fun run() {
                //Check if bluetooth is enabled
                if (!bleAdapter.isEnabled) {
                    stopScanning()
                    handler.removeCallbacks(delayScannRun)
                }
                //Schedule next call of this object in one second
                handler.postDelayed(this, bleCheckTaskInterval)
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun startScanning() {
        scanning.value = true
        bleScanner?.startScan(callBack)
    }

    @SuppressLint("MissingPermission")
    fun stopScanning() {
        scanning.value = false
        handler.removeCallbacks(delayScannRun)
        bleScanner?.stopScan(callBack)
    }

    fun delayScanning(time: Long) {
        val SCAN_PERIOD: Long = if (time > 0) time else DEFAULT_SCAN_PERIOD
        if (scanning.value == false) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(
                delayScannRun,
                SCAN_PERIOD
            )
            startScanning()
        } else {
            stopScanning()
        }
    }

    fun setCallback(callback: ScanCallback) {
        this.callBack = callback
    }

}