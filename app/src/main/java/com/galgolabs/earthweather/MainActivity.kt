package com.galgolabs.earthweather

import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.galgolabs.earthweather.databinding.ActivityMainBinding
//import com.galgolabs.earthweather.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newSingleThreadContext

//@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(newSingleThreadContext("name"))

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment.navController
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard
                R.id.navigation_home, R.id.navigation_city, R.id.navigation_settings
            )
        )

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        setSupportActionBar(binding.myToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        val obserser = Observer<List<MiniClimate>>{
//            print("weather : ${it.get(0)}")
//        }
//        viewModel.allWeather.observe(this, obserser)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onLocationChanged(location: Location) {
    }

}