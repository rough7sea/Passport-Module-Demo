package com.example.myapplication.external.handler

import androidx.lifecycle.LiveData
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.external.entities.LoadResult

interface ObjectBindingHandler<T>{ // Passport -> Tower
    fun getActualObject(): LiveData<LoadResult<T>>
    fun setObjectBinding(objectBinding: T): LiveData<LoadResult<T>>
    fun nextObject(): LiveData<LoadResult<T>>
    fun previousObject(): LiveData<LoadResult<T>>
}