package com.example.myapplication.database.entity

import androidx.room.*
import com.example.myapplication.database.DatabaseConst
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
                Index("coord_id"),
                Index(value = ["idtf", "assetNum", "number"], unique = true),
        ],
        tableName = DatabaseConst.TOWER_TABLE_NAME
)
data class Tower(
        @PrimaryKey(autoGenerate = true)
        val tower_id: Long = 0,
        var passport_id: Long = 0,
        var coord_id: Long?,
        var changeDate: Date = Date(),

        var idtf: String,
        var assetNum: String,
        var stopSeq: Int?,
        var km: Int?,
        var pk: Int?,
        var m: Int?,
        var tfType: String?,
        var turn: String?,
        var radius: String?,
        var number: String,
        var distance: Int?,
        var zigzag: Int?,
        var height: Int?,
        var offset: Int?,
        var grounded: Int?,
        var speed: Int?,
        var suspensionType: String?,
        var catenary: Int?,
        var wireType: String?,
        var countWire: String?,
        var gabarit: String?,
)