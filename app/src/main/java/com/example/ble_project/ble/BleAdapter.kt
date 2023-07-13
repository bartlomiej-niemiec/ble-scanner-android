package com.example.ble_project.ble
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

/* Get instance of BluetoothAdapter */
object BleAdapter: AppCompatActivity() {
    private var bleManager: BluetoothManager? = null
    fun get(context: Context): BluetoothAdapter? {
        if (bleManager == null) {
            bleManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        }
        return bleManager!!.adapter
    }
}

