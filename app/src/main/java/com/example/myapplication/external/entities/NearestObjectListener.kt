package com.example.myapplication.external.entities

sealed class NearestObjectListener<T>(
    val dataList: List<T>
) {
    class OnDiscovered<T>(dataList: List<T>): NearestObjectListener<T>(dataList)
}