package com.example.ble_project

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ble_project.data.DataSource
import com.example.ble_project.data.Beacon

class BeaconDetailViewModel(private val datasource: DataSource) : ViewModel() {

    fun getBeaconForID(id: Long) : Beacon? {
        return datasource.getBeaconForId(id)
    }

}

class BeaconDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BeaconDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BeaconDetailViewModel(
                datasource = DataSource.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}