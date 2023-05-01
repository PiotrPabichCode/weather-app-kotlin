package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private lateinit var utils: Utils
    private lateinit var searchBar: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerView = view.findViewById(R.id.city_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBar = view.findViewById(R.id.searchView)
        utils = Utils()
        val cityList = view.findViewById<RecyclerView>(R.id.city_list)
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = CityListAdapter(ArrayList())
        cityList.layoutManager = layoutManager
        cityList.adapter = adapter

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        GlobalScope.launch {
            val cities = utils.searchCities(query)
            val adapter = CityAdapter(cities)
            withContext(Dispatchers.Main) {
                recyclerView.adapter = adapter
            }
        }
    }
}