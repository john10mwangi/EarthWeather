package com.galgolabs.earthweather.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.*
import com.ekn.gruzer.gaugelibrary.Range
import com.galgolabs.earthweather.MainActivity
import com.galgolabs.earthweather.R
import com.galgolabs.earthweather.databinding.FragmentHomeBinding
import com.galgolabs.earthweather.ui.EarthWeather
import com.galgolabs.earthweather.ui.WeatherWorkScheduler
import com.galgolabs.earthweather.ui.localDB.MiniWeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var mIsRefreshing: Boolean = false
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var preferences: SharedPreferences

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
                        fetch(cityLat!!.toDouble(),
                            cityLng!!.toDouble(),
                            isFromTown = true)
                    }else{
                        fetch(location.latitude, location.longitude)
                    }
                }
            }
    }

    fun fetch(latitude: Double, longitude: Double, isFromTown: Boolean = false){
        homeViewModel.fetchMyLocation(
            lat= latitude,
            lng= longitude,
            preferences = preferences,
            isFromTown = isFromTown
        )
        val data = workDataOf("weather_lat" to latitude, "weather_lon" to longitude)
        val contraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
//        val workRequest = OneTimeWorkRequest.Builder(WeatherWorkScheduler::class.java).build()
        val workRequest = PeriodicWorkRequest.Builder(WeatherWorkScheduler::class.java,
            15, TimeUnit.MINUTES, 0, TimeUnit.MINUTES)
            .setConstraints(contraint)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()
        val workManager = WorkManager.getInstance(requireActivity().applicationContext)
        workManager.enqueueUniquePeriodicWork("weather_work",
            ExistingPeriodicWorkPolicy.KEEP, workRequest)
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
        mIsRefreshing = arguments?.getBoolean("refreshCity") as Boolean
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        val viewModel : HomeViewModel by viewModels {
            HomeViewModelFactory(((requireActivity().applicationContext) as EarthWeather).repository)
        }

        this.homeViewModel = viewModel
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding = DataBindingUtil.bind(view)
        preferences = requireActivity().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
//        val root: View = binding.root
        viewLifecycleOwner.also { binding.lifecycleOwner = it }

        binding.viewModel = homeViewModel

        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, reqPerms[0]) == PackageManager.PERMISSION_GRANTED){
            lastLoc()
        }else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }

        val obs = Observer<NetworkResponse> {
            println("Returned object is : ${it.weatherData.name}")
            homeViewModel.devolve(it.weatherData, preferences)
        }
        homeViewModel.fetchResult.observe(viewLifecycleOwner, obs)

//        val observerAll = Observer<List<MiniWeatherData>> {
//            if (it.size != 0){
//                homeViewModel.populate(it[0])
//                val gson = Gson()
//                val weatherGson = gson.toJson(it[0])
//                preferences.edit().putString("pref_weather", weatherGson).apply()
//            }
//        }
//        homeViewModel.allWeather.observe(viewLifecycleOwner, observerAll)

        val observeInsert = Observer<Long> {
            println("observeInsert : ${it}")
            homeViewModel.fetchById(it)
        }
        homeViewModel.refreshNeeded.observe(viewLifecycleOwner, observeInsert)

        val obsWeather = Observer<MiniWeatherData> {
//            println("obsWeather : ${it}")
            viewModel.populate(it)
        }
        homeViewModel.weather.observe(viewLifecycleOwner, obsWeather)
        return binding.root
    }

    private fun getArgs() {
        cityLng = arguments?.getFloat("myCityLng")
        cityLat = arguments?.getFloat("myCityLat")
        cityName = arguments?.getString("myCityName")

        println("onItemClickedCallback : ${cityName} - LatLng (${cityLat},${cityLng})")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val range = Range()
        range.color = Color.MAGENTA
        range.from = 0.0
        range.to = 100.0
        binding.gauge.addRange(range)
        binding.gauge.minValue = 0.0
        binding.gauge.maxValue = 100.0
//        binding.gauge.value = 45.0
    }

    override fun onResume() {
        super.onResume()
        mIsRefreshing = arguments?.getBoolean("refreshCity") as Boolean
        val mActionBar: ActionBar? = (requireActivity() as MainActivity).supportActionBar
        mActionBar?.hide()
        if (mIsRefreshing){

            println("mIsRefreshing : ${mIsRefreshing}")
            try {
                getArgs()
                fetch(cityLat!!.toDouble(),
                    cityLng!!.toDouble(),
                    isFromTown = true)
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