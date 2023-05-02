package com.example.weatherapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val mainVM by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var homeFragment = HomeFragment()
        val weatherForecastFragment = WeatherForecastFragment()
        val favouriteFragment = FavouriteFragment()
        val addFragment = AddFragment()
        val settingsFragment = SettingsFragment()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null

        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {

                R.id.home -> {
                    if(mainVM.homeCity != homeFragment.getLocation()) {
                        val newHomeFragment = HomeFragment()
                        setCurrentFragment(newHomeFragment)
                        homeFragment = newHomeFragment
                    } else {
                        setCurrentFragment(homeFragment)
                    }
                }
                R.id.forecast -> setCurrentFragment(weatherForecastFragment)
                R.id.favourite -> setCurrentFragment(favouriteFragment)
                R.id.add -> setCurrentFragment(addFragment)
                R.id.settings -> setCurrentFragment(settingsFragment)
            }

            true

        }
    }

    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment, fragment)
        commit()
    }
}