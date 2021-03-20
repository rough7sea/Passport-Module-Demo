package com.example.myapplication.entity

import androidx.room.*
import java.util.*

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = Tower::class,
                    parentColumns = ["tower_id"],
                    childColumns = ["tower_id"]
            ),
            ForeignKey(
                    entity = Coordinate::class,
                    parentColumns = ["coord_id"],
                    childColumns = ["coord_id"]
            )
        ],
        indices = [Index("add_id")],
        tableName = "additional_table"
)
data class Additional(
        @PrimaryKey(autoGenerate = true)
        val add_id: Long = 0,
        val tower_id: Long = 0,
        val coord_id: Long = 0,
        @ColumnInfo(name = "changeDate") var changeDate: Date = Date(),
)