package com.galgolabs.earthweather.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.galgolabs.earthweather.databinding.ItemCityBinding

class CityAdapter(private val cities: ArrayList<City>, private val callback: ItemSelectedCallback): RecyclerView.Adapter<CityAdapter.CityHolder>(), CustomClickCallback {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityHolder(binding)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
        holder.binding.clickCallback = this
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class CityHolder(val binding: ItemCityBinding) : ViewHolder(binding.root){
        fun bind(city: City){
            binding.city = city
        }
    }

    override fun onItemClickedCallback(city: City) {
        callback.onSelection(city)
    }
}