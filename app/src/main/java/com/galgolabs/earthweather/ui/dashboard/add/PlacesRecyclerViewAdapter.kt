package com.galgolabs.earthweather.ui.dashboard.add

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.galgolabs.earthweather.R
import com.galgolabs.earthweather.databinding.FragmentPlacesBinding
import com.galgolabs.earthweather.ui.dashboard.add.placeholder.PlaceholderContent


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PlacesRecyclerViewAdapter(
    private val values: List<PlaceholderContent.PlaceholderItem>
) : RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentPlacesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = "CITY/TOWN"
        holder.contentView.text = "COUNTRY"
    }

    override fun getItemCount(): Int = 30

    inner class ViewHolder(binding: FragmentPlacesBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + "CITY/TOWN" + contentView.text + "COUNTRY"
        }
    }

}