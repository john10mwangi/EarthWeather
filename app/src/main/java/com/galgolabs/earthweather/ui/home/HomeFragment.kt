package com.galgolabs.earthweather.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.galgolabs.earthweather.MainActivity
import com.galgolabs.earthweather.R
import com.galgolabs.earthweather.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {

    private var mIsRefreshing: Boolean = false
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager

    private var cityName: String? = null
    private var cityLat: Float? = 0.0F
    private var cityLng: Float? = 0.0F

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
                    if (mIsRefreshing){
                        homeViewModel.fetchMyLocation(lat= cityLat!!.toDouble(), lng= cityLng!!.toDouble())
                    }else{
                        homeViewModel.fetchMyLocation(lat= location.latitude, lng= location.longitude)
                        println("lastLocation : ${location.latitude} - ${location.longitude}")
                    }
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
        mIsRefreshing = arguments?.get("refreshCity") as Boolean
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

//        val obs = Observer<NetworkResponse> {
//            println("Returned object is : ${it.weatherData.name}")
//            homeViewModel.devolve(it.weatherData)
//        }
//        homeViewModel.fetchResult.observe(viewLifecycleOwner, obs)

        return view
    }

    private fun getArgs() {
        cityLng = arguments?.get("myCityLng") as Float?
        cityLat = arguments?.get("myCityLat") as Float?
        cityName = arguments?.get("myCityName") as String?

        println("onItemClickedCallback : ${cityName} - LatLng (${cityLat},${cityLng})")
    }

    override fun onResume() {
        super.onResume()
        val mActionBar: ActionBar? = (requireActivity() as MainActivity).supportActionBar
        mActionBar?.hide()
        if (mIsRefreshing){
            try {
                getArgs()
            }catch (ex: java.lang.Exception){
                ex.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mActionBar: ActionBar? = (requireActivity() as MainActivity).supportActionBar
        mActionBar?.show()
        _binding = null
    }
}