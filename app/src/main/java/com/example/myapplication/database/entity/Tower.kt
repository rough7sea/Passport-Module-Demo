package com.example.myapplication.database.entity

import androidx.room.*
import com.example.myapplication.database.DatabaseConst
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import java.util.*

@Entity(
        foreignKeys = [
                ForeignKey(
                        entity = Passport::class,
                        parentColumns = ["passport_id"],
                        childColumns = ["passport_id"]
                ),
                ForeignKey(
                        entity = Coordinate::class,
                        parentColumns = ["coord_id"],
                        childColumns = ["coord_id"]
                )
        ],
        indices = [
                Index("tower_id", unique = true),
                Index("passport_id"),
                Index("coord_id")
        ],
        tableName = DatabaseConst.TOWER_TABLE_NAME
)
data class Tower(
        @PrimaryKey(autoGenerate = true)
        val tower_id: Long = 0,
        var coord_id: Long = 0,
        var passport_id: Long = 0,
        var changeDate: Date = Date(),

        var idtf: String = "",
        var assetNum: String = "",
        var stopSeq: Int = 0,
        var km: Int = 0,
        var pk: Int = 0,
        var m: Int = 0,
        var tfType: String = "",
        var turn: String = "",
        var radius: String = "",
        var number: String = "",
        var distance: Int = 0,
        var zigzag: Int = 0,
        var height: Int = 0,
        var offset: Int = 0,
        var grounded: Int = 0,
        var speed: Int = 0,
        var suspensionType: String = "",
        var catenary: Int = 0,
        var wireType: String = "",
        var countWire: String = "",
        var gabarit: String = "",
)