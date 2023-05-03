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
import com.example.weatherapp.databinding.CityItemAddLayoutBinding

class AddCityAdapter(private val cities: List<FavouriteCity>, private val mainVM: MainViewModel) : RecyclerView.Adapter<AddCityAdapter.AddCityViewHolder>() {

    private lateinit var context: Context

    inner class AddCityViewHolder(binding: CityItemAddLayoutBinding) : ViewHolder(binding.root) {
        val nameTV = binding.nameTV
        val tempTV = binding.tempTV
        val addBTN = binding.addBTN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCityViewHolder {
        Log.d("ON CREATE: ", "onCreateViewHolder")
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val rowBinding = CityItemAddLayoutBinding.inflate(inflater, parent, false)
        return AddCityViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: AddCityViewHolder, position: Int) {
        val city = cities[position]
        holder.nameTV.text = "${city.name}, ${city.country}"
        val tempUnit = if (mainVM.isFahrenheitMode) "°F" else "°C"
        holder.tempTV.text = "${city.temp}$tempUnit"
        holder.addBTN.setOnClickListener {
            mainVM.addCity(city)
            Toast.makeText(context, "Added: ${city.name}, ${city.country}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}