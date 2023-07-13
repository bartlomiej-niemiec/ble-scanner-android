package com.example.ble_project.data

/* Beacon representation */
data class Beacon (
    var name: String = "",
    var address: String = "",
    var interval: Float = 0.0F,//periodic advertising interval
    var rssi: String = "", //signal strength in dBm
    var frames: Long = 0,
    val id: Long,
)

/*Convert interval time in units to ms*/
fun convertInterval(units: Int): Float = units * 1.25F
