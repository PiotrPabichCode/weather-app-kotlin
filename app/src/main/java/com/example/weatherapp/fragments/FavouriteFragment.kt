package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.view_models.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.adapters.FavouriteCitiesAdapter
import com.example.weatherapp.databinding.FragmentFavouriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private val mainVM by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentFavouriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteBinding.bind(view)
        binding.rvCityList.layoutManager = LinearLayoutManager(view.context)
        mainVM.updateFavouriteCities()
        binding.rvCityList.adapter = FavouriteCitiesAdapter(mainVM.favouriteCities, mainVM)
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                mainVM.updateFavouriteCities()
                binding.rvCityList.adapter = FavouriteCitiesAdapter(mainVM.favouriteCities, mainVM)
            }
        }
    }
}