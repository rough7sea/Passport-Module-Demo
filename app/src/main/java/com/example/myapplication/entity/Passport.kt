package com.example.myapplication.entity

import androidx.room.*
import java.util.*

@Entity(
        indices = [Index("passport_id")],
        tableName = "passport_table"
)
data class Passport ( // Header
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "passport_id")
        var passport_id: Long = 0,
        @ColumnInfo(name = "changeDate")
        var changeDate: Date = Date(),
        var siteId: Long = 0,
)
