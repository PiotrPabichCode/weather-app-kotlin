package com.example.weatherapp.activities

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.fragments.*

class MainActivity : AppCompatActivity() {
    private val mainVM by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ON CREATE MAIN ACTIVITY", "1")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val homeFragment = HomeFragment()
        Log.d("ON CREATE MAIN ACTIVITY", "NEW HOME FRAGMENT")
        val weatherForecastFragment = WeatherForecastFragment()
        val favouriteFragment = FavouriteFragment()
        val addFragment = AddFragment()
        val settingsFragment = SettingsFragment()

        binding.bottomNavigationView.background = null

        if (savedInstanceState == null) {
            currentFragment = homeFragment
            setCurrentFragment(currentFragment)
        } else {
            currentFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                "currentFragment"
            ) ?: homeFragment
            setCurrentFragment(currentFragment)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.home -> {
                    if (mainVM.updateHomeData) {
                        mainVM.updateHomeData = false
                        HomeFragment()
                    } else {
                        homeFragment
                    }
                }
                R.id.forecast -> weatherForecastFragment
                R.id.favourite -> favouriteFragment
                R.id.add -> addFragment
                R.id.settings -> settingsFragment
                else -> currentFragment
            }

            if (fragment != currentFragment) {
                currentFragment = fragment
                setCurrentFragment(currentFragment)
            }

            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, "currentFragment", currentFragment)
    }

    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(binding.flFragment.id, fragment)
        commit()
    }
}