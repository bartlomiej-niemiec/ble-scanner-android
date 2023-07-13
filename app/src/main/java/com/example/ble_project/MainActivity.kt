package com.example.ble_project

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ble_project.data.Beacon
import com.example.ble_project.details.BeaconDetailActivity
import com.example.ble_project.utility.BeaconsAdapter


const val BEACON_ID: String = "beacon id"
const val RED_COLOR = 0xFF7D0900.toInt()
const val GREEN_COLOR = 0xFF016301.toInt()
const val ScannigActive: String = "Scanning"
const val ScannigNotActive: String = "Press to Scan"
const val SwipeRefreshTriggerDistance: Int = 1000

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var startButton : Button

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this, MainActivityViewModelFactory(this)).get(MainActivityViewModel::class.java)
        setContentView(R.layout.activity_main)
        startButton = findViewById<Button>(R.id.ScanningBtn)
        startButton.setOnClickListener{viewModel.startButtonListener()}
        swipeRefresh = findViewById<Button>(R.id.swiperefresh) as SwipeRefreshLayout
        swipeRefresh.setDistanceToTriggerSync(SwipeRefreshTriggerDistance)
        viewModel.isScanningActive?.observe(this) { enabled ->
            if (enabled) {
                startButton.text = ScannigActive
                startButton.setBackgroundColor(RED_COLOR)
            } else {
                startButton.setBackgroundColor(GREEN_COLOR)
                startButton.text = ScannigNotActive
            }
        }
        val bleList = findViewById<RecyclerView>(R.id.BeaconRecycleView)
        val beaconAdapter = BeaconsAdapter{beacon -> adapterOnClick(beacon)}
        bleList.adapter = beaconAdapter
        viewModel.beaconsLiveData.observe(
            this,
        ) {
            it?.let {
                beaconAdapter.submitList(it)
            }
        }
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.setRefreshing(false)
            viewModel.onRefreshAction()
        }
    }

    private fun adapterOnClick(beacon: Beacon) {
        val intent = Intent(this, BeaconDetailActivity()::class.java)
        intent.putExtra(BEACON_ID, beacon.id)
        startActivity(intent)
    }

}
