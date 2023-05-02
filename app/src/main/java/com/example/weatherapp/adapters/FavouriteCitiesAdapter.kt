package com.example.weatherapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.data.FavouriteCity
import com.example.weatherapp.databinding.CityFavouriteItemLayoutBinding

class FavouriteCitiesAdapter(private var cities: ArrayList<FavouriteCity>, private var viewModel: MainViewModel) : RecyclerView.Adapter<FavouriteCitiesAdapter.FavouriteCitiesViewHolder>() {

    private lateinit var context: Context

    inner class FavouriteCitiesViewHolder(binding: CityFavouriteItemLayoutBinding) : ViewHolder(binding.root) {
        val nameTV = binding.nameTV
        val tempTV = binding.tempTV
        val homeBTN = binding.homeBTN
        val deleteBTN = binding.deleteBTN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCitiesViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val rowBinding = CityFavouriteItemLayoutBinding.inflate(inflater, parent, false)
        return FavouriteCitiesViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: FavouriteCitiesViewHolder, position: Int) {
        val city = cities[position]
        holder.nameTV.text = "${city.name}, ${city.country}"
        holder.tempTV.text = "${city.temp}°C"
        holder.deleteBTN.setOnClickListener {
            cities.removeAt(holder.adapterPosition)
            viewModel.deleteCity(city)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cities.size)
            Toast.makeText(context, "${city.name}, ${city.country} deleted", Toast.LENGTH_SHORT).show()
        }
        holder.homeBTN.setOnClickListener {
            Log.d("HOME CITY: ", "new home City")
            viewModel.setHomeCity(city)
            Toast.makeText(context, "New home: ${city.name}, ${city.country}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}