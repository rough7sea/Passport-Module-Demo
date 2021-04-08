package com.example.myapplication.search.impl

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.App
import com.example.myapplication.external.entities.NearestObjectListener
import com.example.myapplication.external.entities.RequestResult
import com.example.myapplication.search.SearchLocationObjectManager
import com.example.myapplication.utli.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchLocationObjectManagerImpl
//    : SearchLocationObjectManager<Any>
{

//    private var dataBase  = App.getDatabaseManager()
//    private val result = MutableLiveData<RequestResult<Any>>()
//
//    override fun findObjects(gpsLocation: Location, radius: Int): MutableLiveData<RequestResult<Any>> {
//        CoroutineScope(Dispatchers.IO).launch {
//            result.postValue(RequestResult.Loading())
//
//            val coordinate = dataBase.coordinateDao().getCurrentObjectWithParameters(
//                QueryBuilder.buildCoordInRadiusQuery(gpsLocation.longitude, gpsLocation.latitude, radius)
//            )
//
//            if (coordinate == null){
//                result.postValue(RequestResult.Error(Exception("There are no objects in this area")))
//                return@launch
//            }
//
//            val towers = dataBase.towerDao().getByCoordinateId(coordinate.coord_id)
//            val additionals = dataBase.additionalDao().getByCoordinateId(coordinate.coord_id)
//
//            result.postValue(RequestResult.Completed(listOf(towers, additionals)))
//        }
//
//        return result
//    }

//    override fun addListenerToNearestObjects(
//        gpsLocation: Location,
//        radius: Int,
//        listener: (List<Any>) -> Unit) {
//
//        val findObjects = findObjects(gpsLocation, radius)
//
//        findObjects.observe(this, {
//            listener.invoke(it.data)
//        })
//
//    }
}