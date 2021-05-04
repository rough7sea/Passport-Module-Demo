package com.example.datamanager.external.handler

import androidx.lifecycle.LiveData
import com.example.datamanager.external.entities.LoadResult

/**
 * Main handler interface to process all systems objects.
 */
interface ObjectBindingHandler{
    /**
     * Receive actual selected object.
     *
     * @param clazz class mapping parameter to process object.
     * @return [LiveData] with selected object or [RuntimeException] if there are no selected object.
     */
    fun <T> getActualObject(clazz: Class<T>): LiveData<LoadResult<T>>
    /**
     * Set start point object into handler.
     *
     * @param objectBinding object to insert into handler.
     * @return [LiveData] with selected object or [RuntimeException] if there are no selected object.
     */
    fun <T> setObjectBinding(objectBinding: T): LiveData<LoadResult<T>>
    /**
     * * Receive next after selected object.
     *
     * @param clazz class mapping parameter to process object.
     * @return [LiveData] with selected object or [RuntimeException] if there are no selected object.
     */
    fun <T> nextObject(clazz: Class<T>): LiveData<LoadResult<T>>
    /**
     * Receive previous before selected object.
     *
     * @param clazz class mapping parameter to process object.
     * @return [LiveData] with selected object or [RuntimeException] if there are no selected object.
     */
    fun <T> previousObject(clazz: Class<T>): LiveData<LoadResult<T>>
}