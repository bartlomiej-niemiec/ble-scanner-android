package com.example.ble_project.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ble_project.data.Beacon
import com.example.ble_project.BEACON_ID
import com.example.ble_project.BeaconDetailViewModel
import com.example.ble_project.BeaconDetailViewModelFactory
import com.example.ble_project.R

private const val updateTaskInterval: Long = 500

/* Activity for detailed information of selected Beacon */
class BeaconDetailActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateDataRun: Runnable
    private lateinit var beacon: Beacon
    private val detailBeaconModel by viewModels<BeaconDetailViewModel> {
        BeaconDetailViewModelFactory(this)
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beacon_detail)

        var currentBeaconId: Long? = null

        val beaconName: TextView = findViewById(R.id.beacon_name)
        val beaconAddress: TextView = findViewById(R.id.beacon_address)
        val beaconInterval: TextView = findViewById(R.id.beacon_interval)
        val beaconRssi: TextView = findViewById(R.id.beacon_rssi)
        val beaconFrames: TextView = findViewById(R.id.beacon_frames_count)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentBeaconId = bundle.getLong(BEACON_ID)
        }

        fun updateData(beacon : Beacon) {
            beaconName.text = beacon.name
            beaconAddress.text = "Address: " + beacon.address
            beaconInterval.text = "Interval: " + beacon.interval.toString()
            beaconRssi.text = "RSSI: " + beacon.rssi
            beaconFrames.text = "Frames: " + beacon.frames.toString()
        }

        currentBeaconId?.let { id ->
            beacon = detailBeaconModel.getBeaconForID(id)!!
            if (beacon != null) {
                updateData(beacon)
            }
        }

        updateDataRun = object : Runnable {
            override fun run() {
                updateData(beacon)
                handler.postDelayed(this, updateTaskInterval)
            }
        }

        handler.post(updateDataRun)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateDataRun)
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateDataRun)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateDataRun)
    }

}