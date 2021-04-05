package com.example.myapplication.external.handler

import androidx.lifecycle.LiveData
import com.example.myapplication.external.entities.LoadResult

// Tower -> Additional
interface InternalObjectBindingHandler{ // использование при выбранном объекте

    fun <E> getActualInternalObject(): LiveData<LoadResult<E>> // выдать текущий элемент
    fun <E> setInternalObject(internalObject: E) : LiveData<LoadResult<E>> // передаю объект и устанавливаю его как текущий
    // передвижение по истории посещений
    // поменть название
    fun <E> setNextObject(): LiveData<LoadResult<E>>
    fun <E> setPreviousObject(): LiveData<LoadResult<E>>
}