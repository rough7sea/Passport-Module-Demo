package com.example.datamanager.database.entity

import androidx.room.*
import com.example.datamanager.database.DatabaseConst
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
        var siteId: Long?= null,
        var sectionId: String?= null,
        var echName: String?= null,
        var echkName: String?= null,
        var locationId: String?= null,
        var wayAmount: Int?= null,
        var currentWay: String?= null,
        var currentWayId: Long?= null,
        var initialMeter: Int?= null,
        var initialKm: Int?= null,
        var initialPk: Int?= null,
        var initialM: Int?= null,
        var plotLength: Int?= null,
        var suspensionAmount: Int? = null,
)
