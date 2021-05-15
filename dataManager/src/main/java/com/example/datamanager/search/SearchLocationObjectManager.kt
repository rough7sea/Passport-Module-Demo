package com.example.datamanager.search

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.external.entities.RequestResult

/**
 * Search location manager.
 */
interface SearchLocationObjectManager<T> {
    /**
     * Finder objects in radius near gps point.
     *
     * @param radius is measured in meters.
     */
    fun findObjects(gpsLocation: Location, radius: Float): MutableLiveData<RequestResult<T>>

    /**
     * Add listener to track nearest object to current location in radius.
     *
     * @param radius is measured in meters.
     */
    fun addListenerToNearestObjects(radius: Float, listener: (List<T>) -> Unit)

    fun getSearchResult(): MutableLiveData<RequestResult<T>>
}