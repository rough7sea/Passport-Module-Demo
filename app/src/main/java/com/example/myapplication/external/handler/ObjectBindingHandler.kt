package com.example.myapplication.external.handler

import androidx.lifecycle.LiveData
import com.example.myapplication.external.entities.LoadResult

interface ObjectBindingHandler{ // Passport -> Tower
    fun <T> getActualObject(): LiveData<LoadResult<T>>
    fun <T> setObjectBinding(objectBinding: T): LiveData<LoadResult<T>>
    fun <T> nextObject(): LiveData<LoadResult<T>>
    fun <T> previousObject(): LiveData<LoadResult<T>>
}