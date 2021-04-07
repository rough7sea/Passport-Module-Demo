package com.example.myapplication.external.handler

import androidx.lifecycle.LiveData
import com.example.myapplication.external.entities.LoadResult

// Tower -> Additional
interface InternalObjectBindingHandler<E>{
    fun getActualInternalObject(): LiveData<LoadResult<E>>
    fun setInternalObject(internalObject: E) : LiveData<LoadResult<E>>
    fun nextObject(): LiveData<LoadResult<E>>
    fun previousObject(): LiveData<LoadResult<E>>
}