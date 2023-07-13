package com.example.ble_project.utility

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.ble_project.R
import com.example.ble_project.data.Beacon

class BeaconsAdapter(private val onClick: (Beacon) -> Unit) :
    ListAdapter<Beacon, BeaconsAdapter.BeaconViewHolder>(BeaconDiffCallback) {

        /* ViewHolder for Beacon, takes in the inflated view and the onClick behavior. */
        class BeaconViewHolder(itemView: View, val onClick: (Beacon) -> Unit) :
            RecyclerView.ViewHolder(itemView) {
            private val beaconNameTextView: TextView = itemView.findViewById(R.id.beacon_name)
            private var currentBeacon: Beacon? = null

            init {
                itemView.setOnClickListener {
                    currentBeacon?.let {
                        onClick(it)
                    }
                }
            }

            /* Bind beacon name. */
            fun bind(beacon: Beacon) {
                currentBeacon = beacon
                beaconNameTextView.text = beacon.name
            }
        }

        /* Creates and inflates view and return BeaconViewHolder. */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.beacon_item, parent, false)
            return BeaconViewHolder(view, onClick)
        }

        /* Gets current beacon and uses it to bind view. */
        override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
            val beacon = getItem(position)
            holder.bind(beacon)
        }
    }

    object BeaconDiffCallback : DiffUtil.ItemCallback<Beacon>() {
        override fun areItemsTheSame(oldItem: Beacon, newItem: Beacon): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Beacon, newItem: Beacon): Boolean {
            return oldItem.address == newItem.address
        }
}