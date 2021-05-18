package com.example.datamanager.external.handler

import androidx.lifecycle.LiveData
import com.example.datamanager.external.entities.LoadResult

/**
 * Handler interface to process internal objects.
 */
interface InternalObjectBindingHandler<E>{
    /**
     * Receive actual selected object.
     *
     * @return [LiveData] with selected object or [RuntimeException] if there are no selected object.
     */
    fun getActualInternalObject(): LiveData<LoadResult<E>>
    /**
     * Set start point object into handler.
     *
     * @param internalObject object to insert into handler.
     * @return [LiveData] with selected object or [RuntimeException] if there are no selected object.
     */
    fun setInternalObject(internalObject: E) : LiveData<LoadResult<E>>
    /**
     * Receive next after selected object.
     *
     * @return [LiveData] with next object or [RuntimeException] if there are no selected object.
     */
    fun nextInternalObject(): LiveData<LoadResult<E>>
    /**
     * Receive previous before selected object.
     *
     * @return [LiveData] with previous object or [RuntimeException] if there are no selected object.
     */
    fun previousInternalObject(): LiveData<LoadResult<E>>
}