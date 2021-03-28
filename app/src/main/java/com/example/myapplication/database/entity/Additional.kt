package com.example.myapplication.database.entity

import androidx.room.*
import com.example.myapplication.database.DatabaseConst
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
        indices = [
                Index("add_id", unique = true),
                Index("tower_id"),
                Index("coord_id")
        ],
        tableName = DatabaseConst.ADDITIONAL_TABLE_NAME
)
data class Additional(
        @PrimaryKey(autoGenerate = true)
        val add_id: Long = 0,
        var tower_id: Long = 0,
        var coord_id: Long = 0,
        var changeDate: Date = Date(),

        var type: String = "",
        var number: Long = 0,
)