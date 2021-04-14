package com.example.myapplication.external.handler

import androidx.lifecycle.LiveData
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.external.entities.LoadResult

interface ObjectBindingHandler{ // Passport -> Tower
    fun <T> getActualObject(clazz: Class<T>): LiveData<LoadResult<T>>
    fun <T> setObjectBinding(objectBinding: T): LiveData<LoadResult<T>>
    fun <T> nextObject(clazz: Class<T>): LiveData<LoadResult<T>>
    fun <T> previousObject(clazz: Class<T>): LiveData<LoadResult<T>>
}