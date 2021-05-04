package com.example.datamanager.search

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.external.entities.RequestResult

interface SearchLocationObjectManager<T> {
    fun getSearchResult(): MutableLiveData<RequestResult<T>>
    fun findObjects(gpsLocation: Location, radius: Float): MutableLiveData<RequestResult<T>>
    fun addListenerToNearestObjects(radius: Float, listener: (List<T>) -> Unit)
}