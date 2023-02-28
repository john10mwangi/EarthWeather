package com.galgolabs.earthweather.ui.dashboard

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.galgolabs.earthweather.R

import com.galgolabs.earthweather.databinding.FragmentDashboardBinding
import com.galgolabs.earthweather.ui.EarthWeather
import com.galgolabs.earthweather.ui.home.HomeViewModel
import com.galgolabs.earthweather.ui.home.HomeViewModelFactory
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class DashboardFragment : Fragment(), ItemSelectedCallback, SearchView.OnQueryTextListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        dashboardViewModel =
//            ViewModelProvider(this)[DashboardViewModel::class.java]
        dashboardViewModel  =
            HomeViewModelFactory(((requireActivity().applicationContext) as EarthWeather).repository)
                .create(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel.addDummy()

        val cityObs = Observer<ArrayList<City>>{
            var adapter = CityAdapter(it, this@DashboardFragment)
            binding.adapter = adapter
//            binding.placesAdapter = adapter
        }
        dashboardViewModel.cities.observe(viewLifecycleOwner, cityObs)

        val menuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.my_menu, menu)
                val searchItem = menu.findItem(R.id.search_bar)
                val searchBar = searchItem.actionView as SearchView
                searchBar.setOnQueryTextListener(this@DashboardFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        }, viewLifecycleOwner)

        binding.addTown.setOnClickListener {
            val direction = DashboardFragmentDirections.actionNavigationDashboardToPlacesFragment()
            findNavController().navigate(direction)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelection(city: City) {
        val direction = DashboardFragmentDirections.
        actionNavigationDashboardToNavigationHome2(myCityName = city.name, myCityLat = city.lat.toFloat(), myCityLng = city.lng.toFloat(),refreshCity = true)
        findNavController().navigate(directions = direction)
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
        return true
    }
}