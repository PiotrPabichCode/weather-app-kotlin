package com.example.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapp.databinding.CityItemLayoutBinding

class CityListAdapter(private val cities: List<City>) : RecyclerView.Adapter<CityListAdapter.CityListViewHolder>() {

    inner class CityListViewHolder(binding: CityItemLayoutBinding) : ViewHolder(binding.root) {
        val nameTV = binding.nameTV
        val tempTV = binding.tempTV
        val addBTN = binding.addBTN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowBinding = CityItemLayoutBinding.inflate(inflater, parent, false)
        return CityListViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
        val city = cities[position]
        holder.nameTV.text = "${city.name}, ${city.country}"
        holder.tempTV.text = city.temp.toString() + "°C"
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}