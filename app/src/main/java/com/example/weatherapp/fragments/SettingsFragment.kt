package com.example.weatherapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.view_models.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.activities.dataStore
import com.example.weatherapp.databinding.FragmentSettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var dataStore: DataStore<Preferences>
    private val mainVM by activityViewModels<MainViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        dataStore = requireContext().dataStore
        loadPreferences()

        binding.fahrenheitSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                mainVM.isFahrenheitMode = true
                CoroutineScope(Dispatchers.Main).launch {
                    val dataStoreKey = booleanPreferencesKey("fahrenheit_mode")
                    dataStore.edit { preferences ->
                        preferences[dataStoreKey] = true
//                        Toast.makeText(context, "Your unit: Fahrenheit", Toast.LENGTH_SHORT).show()
                    }
                }
                // Switch checked
            } else {
                mainVM.isFahrenheitMode = false
                CoroutineScope(Dispatchers.Main).launch {
                    val dataStoreKey = booleanPreferencesKey("fahrenheit_mode")
                    dataStore.edit { preferences ->
                        preferences[dataStoreKey] = false
//                        Toast.makeText(context, "Your unit: Celsius", Toast.LENGTH_SHORT).show()
                    }
                }
                // Switch not checked
            }
        }

        binding.refreshET.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                CoroutineScope(Dispatchers.Main).launch {
                    mainVM.intervalTime = binding.refreshET.text.toString().toIntOrNull() ?: 0
                    val dataStoreKey = intPreferencesKey("interval_time")
                    dataStore.edit { preferences ->
                        preferences[dataStoreKey] = mainVM.intervalTime
//                        Toast.makeText(context, "Your interval: ${mainVM.intervalTime}s", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }

    private fun loadPreferences() {
        binding.settingsLayout.visibility = View.GONE
        CoroutineScope(Dispatchers.Main).launch {
            val preferences = dataStore.data.first()

            val fahrenheitModeKey = booleanPreferencesKey("fahrenheit_mode")
            val intervalKey = intPreferencesKey("interval_time")

            mainVM.isFahrenheitMode = preferences[fahrenheitModeKey] ?: false
            binding.fahrenheitSwitch.isChecked = mainVM.isFahrenheitMode

            mainVM.intervalTime = preferences[intervalKey] ?: 0
            binding.refreshET.setText(mainVM.intervalTime.toString())
        }
        binding.settingsLayout.visibility = View.VISIBLE
    }
}