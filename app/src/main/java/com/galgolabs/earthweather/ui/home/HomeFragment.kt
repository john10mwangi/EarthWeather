package com.galgolabs.earthweather.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.galgolabs.earthweather.R
import com.galgolabs.earthweather.databinding.FragmentHomeBinding
import com.galgolabs.earthweather.ui.ApiModule
import com.galgolabs.earthweather.ui.WebServiceInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager

    private lateinit var homeViewModel : HomeViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                lastLoc()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                lastLoc()
            } else -> {
            // No location access granted.
        }
        }
    }

    @SuppressLint("MissingPermission")
    fun lastLoc(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    homeViewModel.fetchMyLocation(lat= location.latitude, lng= location.longitude)
                    println("lastLocation : ${location.latitude} - ${location.longitude}")
                }
            }
    }

    val reqPerms = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding = DataBindingUtil.bind(view)
//        val root: View = binding.root
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = homeViewModel



        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, reqPerms[0]) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(requireActivity(), "granted", Toast.LENGTH_SHORT).show()
            lastLoc()
        }else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }

        val obs = Observer<NetworkResponse> {
            println("Returned object is : ${it.weatherData.name}")
            homeViewModel.devolve(it.weatherData)
        }
        homeViewModel.fetchResult.observe(viewLifecycleOwner, obs)


//        val nameObs = Observer<String> {
//            binding.cityText.text = it
//        }
//        homeViewModel.locationName.observe(viewLifecycleOwner, nameObs)

//        return root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}