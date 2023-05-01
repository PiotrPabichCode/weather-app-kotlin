package com.example.weatherapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentFavouriteBinding
import kotlinx.coroutines.*

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private lateinit var utils: Utils

    private lateinit var binding: FragmentFavouriteBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteBinding.bind(view)
        utils = Utils()
        binding.rvCityList.layoutManager = LinearLayoutManager(view.context)
        binding.rvCityList.adapter = CityListAdapter(ArrayList())

        handleSearchView()
    }

    private fun handleSearchView() {
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
                binding.rvCityList.adapter = CityListAdapter(cities)
            }
        }
    }
}