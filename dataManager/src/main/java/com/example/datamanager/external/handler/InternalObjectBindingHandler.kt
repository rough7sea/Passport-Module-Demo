package com.example.datamanager.external.handler

import androidx.lifecycle.LiveData
import com.example.datamanager.external.entities.LoadResult

interface InternalObjectBindingHandler<E>{
    fun getActualInternalObject(): LiveData<LoadResult<E>>
    fun setInternalObject(internalObject: E) : LiveData<LoadResult<E>>
    fun nextInternalObject(): LiveData<LoadResult<E>>
    fun previousInternalObject(): LiveData<LoadResult<E>>
}