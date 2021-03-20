package com.example.myapplication.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity(
        indices = [Index(value = ["coord_id"], unique = true),
                  Index(value = ["longitude", "latitude"], unique = true)],
        tableName = "coordinate_table"
)
data class Coordinate(
        @PrimaryKey(autoGenerate = true)
        val coord_id: Long = 0,
        @ColumnInfo(name = "changeDate")
        var changeDate: Date = Date(),
        var longitude: Int,
        var latitude: Int,
)