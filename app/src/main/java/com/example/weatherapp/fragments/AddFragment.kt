package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.view_models.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.adapters.AddCityAdapter
import com.example.weatherapp.databinding.FragmentAddBinding
import com.example.weatherapp.utils.Utils.searchCities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFragment : Fragment(R.layout.fragment_add) {

    private val mainVM by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentAddBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        binding.rvCityList.layoutManager = LinearLayoutManager(view.context)
        binding.rvCityList.adapter = AddCityAdapter(emptyList(), mainVM)

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
            try {
                val cities = searchCities(query)
                withContext(Dispatchers.Main) {
                    binding.rvCityList.adapter = AddCityAdapter(cities, mainVM)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "No internet connection. Try again later", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}