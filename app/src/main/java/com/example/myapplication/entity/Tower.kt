package com.example.myapplication.entity

import androidx.room.*
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
        indices = [Index("tower_id")],
        tableName = "tower_table"
)
data class Tower(
        @PrimaryKey(autoGenerate = true)
        val tower_id: Long = 0,
        val coord_id: Long = 0,
        @ColumnInfo(name = "passport_id") var passport_id: Long = 0,
        @ColumnInfo(name = "changeDate") var changeDate: Date = Date(),
        @ColumnInfo(name = "idtf") var idtf: String = "",
)