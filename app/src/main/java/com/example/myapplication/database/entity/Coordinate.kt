package com.example.myapplication.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.myapplication.database.DatabaseConst
import java.util.*

@Entity(
        indices = [
                Index(value = ["coord_id"], unique = true),
                Index(value = ["longitude", "latitude"], unique = true)
        ],
        tableName = DatabaseConst.COORDINATE_TABLE_NAME
)
data class Coordinate(
        @PrimaryKey(autoGenerate = true)
        val coord_id: Long = 0,
        var changeDate: Date = Date(),

        var longitude: Double,
        var latitude: Double,
)