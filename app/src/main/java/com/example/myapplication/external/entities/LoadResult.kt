package com.example.myapplication.external.entities

sealed class LoadResult<T>(
    val data: T?,
    val error: Exception? = null
) {
    class Success<T>(data: T): LoadResult<T>(data)
    class Loading<T>(data: T? = null): LoadResult<T>(data)
    class Error<T>(error: Exception, data: T? = null): LoadResult<T>(data, error)
}
