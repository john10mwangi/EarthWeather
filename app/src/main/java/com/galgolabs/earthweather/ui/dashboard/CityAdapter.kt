package com.galgolabs.earthweather.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.galgolabs.earthweather.R
import com.galgolabs.earthweather.databinding.ItemCityBinding
import java.util.zip.Inflater

class CityAdapter(private val cities: ArrayList<City>): RecyclerView.Adapter<CityAdapter.CityHolder>(), CustomClickCallback {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityHolder(binding)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
//        holder.itemView.setOnI
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class CityHolder(private val binding: ItemCityBinding) : ViewHolder(binding.root){
        val itemView = binding.root
        fun bind(city: City){
            binding.city = city
        }
    }

    override fun onItemClickedCallback(city: City) {
        println("onItemClickedCallback : ${city.name}")
    }
}