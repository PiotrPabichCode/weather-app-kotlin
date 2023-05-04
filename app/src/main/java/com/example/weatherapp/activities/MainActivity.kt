package com.example.weatherapp.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.view_models.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.fragments.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment
    private lateinit var mainVM: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainVM = ViewModelProvider(this).get(MainViewModel::class.java)
        loadPreferences()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val homeFragment = HomeFragment()
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

    private fun loadPreferences() {
        GlobalScope.launch {
            val homeCity = readString("home_city")
            mainVM.homeCity = homeCity ?: "Poznan"

            val intervalTime = readInt("interval_time")
            mainVM.intervalTime = intervalTime ?: 0

            val isFahrenheitMode = readBoolean("fahrenheit_mode")
            mainVM.isFahrenheitMode = isFahrenheitMode ?: false
        }
    }

    private suspend fun readString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun readInt(key: String): Int? {
        val dataStoreKey = intPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun readBoolean(key: String): Boolean? {
        val dataStoreKey = booleanPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
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