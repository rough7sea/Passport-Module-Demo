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
import com.example.datamanager.external.entities.RequestResult
import com.example.datamanager.search.SearchLocationObjectManager
import com.example.datamanager.utli.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchLocationObjectManagerImpl(
    private val locationManager: LocationManager,
    private val context: Context,
    private val appDatabase: AppDatabase
) : SearchLocationObjectManager<Any> {

    private val result = MutableLiveData<RequestResult<Any>>()

    override fun findObjects(gpsLocation: Location, radius: Int): MutableLiveData<RequestResult<Any>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(RequestResult.Loading())

            val coordinate = appDatabase.coordinateDao().getCurrentObjectWithParameters(
                QueryBuilder.buildCoordInRadiusQuery(gpsLocation.longitude, gpsLocation.latitude, radius)
            )

            if (coordinate == null){
                result.postValue(RequestResult.Error(RuntimeException("There are no objects in this area")))
                return@launch
            }

            val towers = appDatabase.towerDao().getByCoordinateId(coordinate.coord_id)
            val additionals = appDatabase.additionalDao().getByCoordinateId(coordinate.coord_id)

            result.postValue(RequestResult.Completed(listOf(towers, additionals)))
        }

        return result
    }

    override fun addListenerToNearestObjects(gpsLocation: Location, radius: Int, listener: (List<Any>) -> Unit) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw RuntimeException("Don't have location permission")
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000 * 10L, 10f){ it ->

            val findObjects = findObjects(it, radius)
            findObjects.observeForever {
                it.data?.let { it1 -> listener.invoke(it1) }
            }
            Log.i("TEST", "Longitude : ${it.longitude}, Latitude : ${it.latitude}")
        }
    }
}