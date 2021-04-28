package com.example.datamanager.external.entities

sealed class RequestResult<T>(
    val data: List<T>? = null,
    val progress: Int? = 0,
    val error: Exception? = null
) {
    class Completed<T>(data: List<T>?) : RequestResult<T>(data)
    class Loading<T> : RequestResult<T>()
    class Progress<T>(progress: Int) : RequestResult<T>(progress = progress)
    class Canceled<T> : RequestResult<T>()
    class Error<T>(error: Exception) : RequestResult<T>(error = error)
}