package com.example.myapplication.converter

import androidx.room.TypeConverter
import java.util.*

class Converter {
    @TypeConverter
    fun getDateFromTimestamp(value: Long?) = value?.let { Date(value) }
    @TypeConverter
    fun getTimestampFromDate(value: Date?) = value?.time

}