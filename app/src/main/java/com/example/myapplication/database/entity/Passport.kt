package com.example.myapplication.database.entity

import androidx.room.*
import com.example.myapplication.database.DatabaseConst
import java.util.*

@Entity(
        indices = [
                Index("passport_id", unique = true),
                Index("sectionName", unique = true),
        ],
        tableName = DatabaseConst.PASSPORT_TABLE_NAME
)
data class Passport (
        @PrimaryKey(autoGenerate = true)
        val passport_id: Long = 0,
        var changeDate: Date = Date(),

        var sectionName: String,
        var siteId: Long?,
        var sectionId: String?,
        var echName: String?,
        var echkName: String?,
        var locationId: String?,
        var wayAmount: Int?,
        var currentWay: String?,
        var currentWayId: Long?,
        var initialMeter: Int?,
        var initialKm: Int?,
        var initialPk: Int?,
        var initialM: Int?,
        var plotLength: Int?,
        var suspensionAmount: Int?,
)
