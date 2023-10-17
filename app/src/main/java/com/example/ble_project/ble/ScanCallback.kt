package com.example.ble_project.ble

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ble_project.data.BeaconBuilder
import com.example.ble_project.data.DataSource
import com.example.ble_project.data.convertInterval


class ScanCallback(private val dataSource: DataSource) : ScanCallback() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)
        val address = result.device.address
        if (dataSource.getBeaconForAddress(address) == null){
            dataSource.addBeacon(BeaconBuilder.buildBeacon(result, dataSource.getListSize()))
        }
        else {
            dataSource.updateBeaconData(
                address=address,
                interval=convertInterval(result.periodicAdvertisingInterval),
                rssi=result.rssi)
        }
    }
}