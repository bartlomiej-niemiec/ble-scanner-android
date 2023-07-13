package com.example.ble_project.data

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi

/*Build Beacon for given ScanResult and Number*/
class BeaconBuilder {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("MissingPermission")
        fun buildBeacon(result: ScanResult, number: Int) : Beacon {
            var name : String? = result.device.name

            if (name?.lowercase() == "null" || name.isNullOrEmpty()) {
                name = "Beacon $number"
            }

            if (result.scanRecord?.getManufacturerSpecificData(0x0059) != null) {
                name = "NRF51822"
            }

            return Beacon(
                name=name,
                address=result.device.address,
                interval=convertInterval(result.periodicAdvertisingInterval),
                rssi=result.rssi.toString() + " dBm",
                id = number.toLong(),
                frames = 1
            )
        }
    }
}