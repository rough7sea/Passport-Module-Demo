package com.example.myapplication.fragments.testfragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.datamanager.search.SearchLocationObjectManager
import com.example.myapplication.App
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_search_test.view.*
import kotlinx.android.synthetic.main.tower_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchTestFragment : Fragment() {

    private val searchLocationObjectManager : SearchLocationObjectManager<Any> by lazy { App.getDataManager() }
    private val locationManager: LocationManager by lazy { App.getLocationManger() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_test, container, false)

        searchLocationObjectManager.getSearchResult().observe(viewLifecycleOwner, {
            it.data?.forEach { obj ->
                Log.i("SEARCH_TEST_FRAGMENT", obj.toString())
            }
            view.textView_object_count.text = it.data?.size.toString()
            requireActivity().runOnUiThread {
                Toast.makeText(activity, "Receive [${it.data?.size}] objects.", Toast.LENGTH_SHORT).show()
            }
        })

        view.listen_button.setOnClickListener {
            val radius = view.edit_radius_field.text.toString()

            view.edit_longitude_field.text = "Nan"
            view.edit_latitude_field.text = "Nan"
            if (radius.isNotEmpty()) {
                searchLocationObjectManager.addListenerToNearestObjects(radius.toFloat()) {
                    view.textView_object_count.text = it.size.toString()
                    it.forEach { obj ->
                        Log.i("SEARCH_TEST_FRAGMENT", obj.toString())
                    }
                    requireActivity().runOnUiThread {
                        Toast.makeText(activity, "Receive [${it.size}] objects.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Radius is empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.search_button.setOnClickListener {
            val radius = view.edit_radius_field.text.toString()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                Log.e("SEARCH_LOCATION_MANAGER", "Location permission denied")
                throw RuntimeException("Location permission denied")
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10L, radius.toFloat(), {})
            CoroutineScope(Dispatchers.IO).launch {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (radius.isNotEmpty() && location != null){

                    view.edit_longitude_field.text = location.longitude.toString()
                    view.edit_latitude_field.text = location.latitude.toString()

                    Log.i("SEARCH_TEST_FRAGMENT",
                        "Current location [long:${location.longitude} & lat:${location.latitude}]")

                    searchLocationObjectManager.findObjects(location, radius.toFloat())
                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(activity, "Radius or location is empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }
}