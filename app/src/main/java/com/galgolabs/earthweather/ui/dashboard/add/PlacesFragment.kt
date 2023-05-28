package com.galgolabs.earthweather.ui.dashboard.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.galgolabs.earthweather.databinding.FragmentPlacesListBinding
import com.galgolabs.earthweather.ui.EarthWeather
import com.galgolabs.earthweather.ui.dashboard.City
import com.galgolabs.earthweather.ui.dashboard.DashboardViewModel
import com.galgolabs.earthweather.ui.dashboard.ItemSelectedCallback
import com.galgolabs.earthweather.ui.dashboard.add.placeholder.CitiesRecyclerViewAdapter
import com.galgolabs.earthweather.ui.home.HomeViewModelFactory
import com.galgolabs.earthweather.ui.home.NetworkResponse2
import com.galgolabs.earthweather.ui.home.TownData
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class PlacesFragment : Fragment(), SearchView.OnQueryTextListener,
    ItemSelectedCallback  {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentPlacesListBinding
    private var columnCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesListBinding.inflate(inflater, container, false)

        dashboardViewModel =
            HomeViewModelFactory(((requireActivity().applicationContext) as EarthWeather).repository)
                .create(DashboardViewModel::class.java)

        binding.searchBar.setOnQueryTextListener(this)


        val queryObs = Observer<NetworkResponse2> {
            println("Returned object is : ${it.data}")
            val towns: List<TownData>  = it.data
            binding.searchList.visibility = View.VISIBLE
            binding.searchList.adapter = PlacesRecyclerViewAdapter(towns).apply {
                setCallback(this@PlacesFragment)
            }

        }
        dashboardViewModel.queryResult.observe(viewLifecycleOwner, queryObs)

        // Set the adapter
        with(binding.list) {
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            )
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
        binding.list.layoutManager = GridLayoutManager(context, 2)
        val obs = Observer<List<City>> {
            binding.list.adapter = CitiesRecyclerViewAdapter(it).apply {
                setCallback(this@PlacesFragment)
            }
        }
        dashboardViewModel.cities.observe(viewLifecycleOwner, obs)
        dashboardViewModel.addDummy()
        return binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PlacesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            if (!TextUtils.isEmpty(query.trim()) && query.trim().length > 2){
                viewLifecycleOwner.lifecycleScope.launch {
                    dashboardViewModel.fetchTown(query)
                }
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        binding.list.visibility = View.INVISIBLE
        return true
    }

    override fun onSelection(city: City) {
        super.onSelection(city)
        println("onSelection : $city")
        saveArgrs(name = city.name, lat = city.lat, lng=city.lng)
    }

    override fun onSelection(town: TownData) {
        super.onSelection(town)
        println("onSelection : $town")
        saveArgrs(name = town.display_name, lat = town.lat.toDouble(), lng=town.lon.toDouble())
    }

    private fun saveArgrs(name: String, lat: Double, lng: Double) {
        val direction = PlacesFragmentDirections.actionPlacesFragmentToNavigationCity(
            myCityName = name, myCityLat = lat.toFloat(), myCityLng = lng.toFloat())
        findNavController().navigate(direction)
    }
}