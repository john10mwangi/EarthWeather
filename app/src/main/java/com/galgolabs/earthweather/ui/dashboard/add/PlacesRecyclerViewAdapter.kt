package com.galgolabs.earthweather.ui.dashboard.add

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import com.galgolabs.earthweather.databinding.FragmentPlacesBinding
import com.galgolabs.earthweather.ui.dashboard.ItemSelectedCallback
import com.galgolabs.earthweather.ui.home.TownData


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PlacesRecyclerViewAdapter(
    private val values: List<TownData>
) : RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder>() {

    private lateinit var callback: ItemSelectedCallback

    fun setCallback(callback: ItemSelectedCallback){
        this.callback = callback
    }

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
        holder.idView.text = item.display_name
        holder.contentView.visibility = GONE

        holder.itemView.setOnClickListener { callback.onSelection(item) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentPlacesBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + "CITY/TOWN" + contentView.text + "COUNTRY"
        }
    }

}