package com.galgolabs.earthweather.ui.dashboard.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.galgolabs.earthweather.R
import com.galgolabs.earthweather.databinding.FragmentPlacesListBinding
import com.galgolabs.earthweather.ui.EarthWeather
import com.galgolabs.earthweather.ui.dashboard.City
import com.galgolabs.earthweather.ui.dashboard.DashboardViewModel
import com.galgolabs.earthweather.ui.dashboard.add.placeholder.PlaceholderContent
import com.galgolabs.earthweather.ui.home.HomeViewModelFactory

/**
 * A fragment representing a list of Items.
 */
class PlacesFragment : Fragment() {

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
        binding = FragmtenPlacesListBinding.inflate(inflater, container, false)
        val viewModel : DashboardViewModel by viewModels{
            HomeViewModelFactory(((requireActivity()).applicationContext as EarthWeather).repository)
        }

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
            binding.list.adapter = PlacesRecyclerViewAdapter(it)
        }
        viewModel.cities.observe(viewLifecycleOwner, obs)
        viewModel.addDummy()
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
}