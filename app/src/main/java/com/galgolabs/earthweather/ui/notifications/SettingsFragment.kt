package com.galgolabs.earthweather.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.galgolabs.earthweather.R

class SettingsFragment : PreferenceFragmentCompat(), OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_layout, rootKey)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        super.onCreateView(inflater, container, savedInstanceState)
//        return view
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireActivity().applicationContext)
        val langDropDownPreference = preferenceManager.findPreference<DropDownPreference>("weather_lang")
        val unitsDropDownPreference = preferenceManager.findPreference<DropDownPreference>("weather_unit")
        val locationLangDropDownPreference = preferenceManager.findPreference<DropDownPreference>("location_lang")
        unitsDropDownPreference?.onPreferenceChangeListener = this
        langDropDownPreference?.onPreferenceChangeListener = this
        locationLangDropDownPreference?.onPreferenceChangeListener = this
    }



    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        when (preference.key){
            "weather_lang" -> {
                displayValue(preference.key, newValue)
            }
            "weather_unit" -> {
                displayValue(preference.key, newValue)
            }
            "location_lang" -> {
                displayValue(preference.key, newValue)
            }
        }
        return true
    }

    private fun displayValue(key: String, newValue: Any?) {
        println("Preference : $key")
        println("Value : ${newValue.toString()}")
    }
}