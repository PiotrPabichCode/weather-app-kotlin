package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarItemView

class MainActivity : AppCompatActivity() {

    val CITY = "Łódź"
    val weather_api = "c1b8cca3f8e0025563835d33cad2543e"

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val favouriteFragment = FavouriteFragment()
        val settingsFragment = SettingsFragment()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {

                R.id.home -> setCurrentFragment(homeFragment)

                R.id.favourite -> setCurrentFragment(favouriteFragment)

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