package com.example.myapplication.database.entity

import androidx.room.*
import com.example.myapplication.database.DatabaseConst
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import java.util.*

@Entity(
        indices = [
                Index("passport_id", unique = true)
        ],
        tableName = DatabaseConst.PASSPORT_TABLE_NAME
)
data class Passport (
        @PrimaryKey(autoGenerate = true)
        val passport_id: Long = 0,
        var changeDate: Date = Date(),

        var sectionName: String = "",
        var siteId: Long = 0,
        var sectionId: String = "",
        var echName: String = "",
        var echkName: String = "",
        var locationId: String = "",
        var wayAmount: Int = 0,
        var currentWay: String = "",
        var currentWayId: Long = 0,
        var initialMeter: Int = 0,
        var initialKm: Int = 0,
        var initialPk: Int = 0,
        var initialM: Int = 0,
        var plotLength: Int = 0,
        var suspensionAmount: Int = 0,
)
