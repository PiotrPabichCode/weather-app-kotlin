package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FragmentFavouriteBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.*

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private lateinit var utils: Utils

    private lateinit var binding: FragmentFavouriteBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteBinding.bind(view)
        utils = Utils()
        binding.rvCityList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCityList.adapter = CityListAdapter(ArrayList())

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateSearchResults(newText.toString())
                return false
            }
        })
    }

    fun updateSearchResults(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val cities = utils.searchCities(query)
            withContext(Dispatchers.Main) {
                binding.rvCityList.adapter = CityAdapter(cities)
            }
        }
    }
}