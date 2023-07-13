package com.example.ble_project.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSource {

    private val beaconList: List<Beacon> = listOf()
    private val beaconLiveData = MutableLiveData(beaconList)

    fun getListSize() : Int = beaconLiveData.value?.size!! + 1

    /* Adds beacon to liveData and posts value. */
    fun addBeacon(beacon: Beacon) {
        val currentList = beaconLiveData.value
        if (currentList == null) {
            beaconLiveData.postValue(listOf(beacon))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, beacon)
            beaconLiveData.postValue(updatedList)
        }
    }

    /* Removes beacon from liveData and posts value. */
    fun removeBeacon(beacon: Beacon) {
        val currentList = beaconLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(beacon)
            beaconLiveData.postValue(updatedList)
        }
    }

    /* Removes all beacons from liveData and posts value. */
    fun removeAllBeacons() {
        val currentList = beaconLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.clear()
            beaconLiveData.postValue(updatedList)
        }
    }

    /* Returns beacon given an ID. */
    fun getBeaconForAddress(address: String): Beacon? {
        beaconLiveData.value?.let { beacons ->
            return beacons.firstOrNull{ it.address == address}
        }
        return null
    }

    /*Get Beacon in List from its ID*/
    fun getBeaconForId(id: Long): Beacon? {
        beaconLiveData.value?.let { beacons ->
            return beacons.firstOrNull{ it.id == id}
        }
        return null
    }

    /* Update beacons data for each new scan from liveData and posts value. */
    fun updateBeaconData(address: String, rssi: Int, interval: Float) {
        val currentList = beaconLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            for (beacon: Beacon in updatedList) {
                if (beacon.address == address) {
                    beacon.frames += 1
                    beacon.rssi = "$rssi dBm"
                    beacon.interval = interval
                    beaconLiveData.postValue(updatedList)
                    break
                }
            }
        }
    }

    /*Get beacons live data list*/
    fun getBeaconList(): LiveData<List<Beacon>> {
        return beaconLiveData
    }

    /* Data Source its instantiated only once */
    companion object {
        private var INSTANCE: DataSource? = null
        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}