package com.example.datamanager.search.impl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.external.entities.RequestResult
import com.example.datamanager.search.SearchLocationObjectManager
import com.example.datamanager.utli.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchLocationObjectManagerImpl(
    private val locationManager: LocationManager,
    private val context: Context,
    appDatabase: AppDatabase
) : SearchLocationObjectManager<Any> {

    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())
    private val coordinateRepository = CoordinateRepository(appDatabase.coordinateDao())
    private val result = MutableLiveData<RequestResult<Any>>()

    override fun getSearchResult() = result

    override fun findObjects(gpsLocation: Location, radius: Float): MutableLiveData<RequestResult<Any>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(RequestResult.Loading())

            val objects = mutableListOf<Any>()

            coordinateRepository.findAll().forEach {
                if (Utils.distanceInMeters(it.latitude, it.longitude, gpsLocation.latitude, gpsLocation.longitude) <= radius){
                    objects.addAll(towerRepository.findAllByCoordinateId(it.coord_id))
                    objects.addAll(additionalRepository.findAllByCoordinateId(it.coord_id))
                }
            }

            if (objects.isEmpty()){
                result.postValue(RequestResult.Error(RuntimeException("There are no objects in this area")))
                return@launch
            }

            Log.i("SEARCH_LOCATION_MANAGER",
                "Find [${objects.size}] in location " +
                        "[long:${gpsLocation.longitude} & lat:${gpsLocation.latitude}] with radius [$radius]")
            result.postValue(RequestResult.Completed(objects))
        }

        return result
    }

    /**
     * Method must be in main thread.
     */
    override fun addListenerToNearestObjects(radius: Float, listener: (List<Any>) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            Log.e("SEARCH_LOCATION_MANAGER", "Location permission denied")
            throw RuntimeException("Location permission denied")
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            100 * 10L, radius){ location ->

            val findObjects = findObjects(location, radius)
            findObjects.observeForever {
                it.data?.let { it1 -> listener.invoke(it1) }
            }
            Log.i("SEARCH_LOCATION_MANAGER", "Current location [long:${location.longitude} & lat:${location.latitude}]")
        }
    }
}