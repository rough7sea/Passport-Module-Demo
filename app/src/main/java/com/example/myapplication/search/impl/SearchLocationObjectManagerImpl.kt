package com.example.myapplication.search.impl

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.App
import com.example.myapplication.external.entities.RequestResult
import com.example.myapplication.search.SearchLocationObjectManager
import com.example.myapplication.utli.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchLocationObjectManagerImpl(
    private val locationManager: LocationManager
) : SearchLocationObjectManager<Any> {

    private var dataBase  = App.getDatabase()
    private val result = MutableLiveData<RequestResult<Any>>()

    override fun findObjects(gpsLocation: Location, radius: Int): MutableLiveData<RequestResult<Any>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(RequestResult.Loading())

            val coordinate = dataBase.coordinateDao().getCurrentObjectWithParameters(
                QueryBuilder.buildCoordInRadiusQuery(gpsLocation.longitude, gpsLocation.latitude, radius)
            )

            if (coordinate == null){
                result.postValue(RequestResult.Error(Exception("There are no objects in this area")))
                return@launch
            }

            val towers = dataBase.towerDao().getByCoordinateId(coordinate.coord_id)
            val additionals = dataBase.additionalDao().getByCoordinateId(coordinate.coord_id)

            result.postValue(RequestResult.Completed(listOf(towers, additionals)))
        }

        return result
    }

    override fun addListenerToNearestObjects(gpsLocation: Location, radius: Int, listener: (List<Any>) -> Unit) {

        if (ActivityCompat.checkSelfPermission(
                App.getAppContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                App.getAppContext(),
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