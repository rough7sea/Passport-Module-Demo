package com.example.datamanager.external.handler

import androidx.lifecycle.LiveData
import com.example.datamanager.external.entities.LoadResult

interface ObjectBindingHandler{
    fun <T> getActualObject(clazz: Class<T>): LiveData<LoadResult<T>>
    fun <T> setObjectBinding(objectBinding: T): LiveData<LoadResult<T>>
    fun <T> nextObject(clazz: Class<T>): LiveData<LoadResult<T>>
    fun <T> previousObject(clazz: Class<T>): LiveData<LoadResult<T>>
}