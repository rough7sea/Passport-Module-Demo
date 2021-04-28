package com.example.datamanager.search

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.external.entities.RequestResult

interface SearchLocationObjectManager<T> {
    fun findObjects(gpsLocation: Location, radius: Int): MutableLiveData<RequestResult<T>>
    fun addListenerToNearestObjects(gpsLocation: Location, radius: Int, listener: (List<Any>) -> Unit)
}