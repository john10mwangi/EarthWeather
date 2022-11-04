package com.galgolabs.earthweather.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.galgolabs.earthweather.R

import com.galgolabs.earthweather.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(), ItemSelectedCallback {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel.addDummy()

        val cityObs = Observer<ArrayList<City>>{
            var adapter = CityAdapter(it, this@DashboardFragment)
            binding.adapter = adapter
        }
        dashboardViewModel.cities.observe(viewLifecycleOwner, cityObs)

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
}