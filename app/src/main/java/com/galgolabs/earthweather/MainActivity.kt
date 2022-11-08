package com.galgolabs.earthweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.galgolabs.earthweather.databinding.ActivityMainBinding
//import com.galgolabs.earthweather.databinding.ActivityMainBinding
import com.galgolabs.earthweather.ui.ApiModule
import com.galgolabs.earthweather.ui.EarthWeather
import com.galgolabs.earthweather.ui.WebServiceInterface
import com.galgolabs.earthweather.ui.localDB.MiniClimate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.coroutineContext

//@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(newSingleThreadContext("name"))

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        val viewModel : MainViewModel by viewModels {
            MainViewModelFactory((application as EarthWeather).repository)
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        setSupportActionBar(binding.myToolbar)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val obserser = Observer<List<MiniClimate>>{
            print("weather : ${it.get(0)}")
        }
        viewModel.allWeather.observe(this, obserser)
        try {
            val climate = MiniClimate(3163858, "Zocca",200,10000,1661870592)
            scope.launch{
                viewModel.insertData(climate)
            }
        }catch (ex: java.lang.Exception){
            ex.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onLocationChanged(location: Location) {
    }

}