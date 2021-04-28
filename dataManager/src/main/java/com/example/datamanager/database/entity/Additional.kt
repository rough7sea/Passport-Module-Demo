package com.example.datamanager.database.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.datamanager.database.DatabaseConst
import java.util.*

@Entity(
        foreignKeys = [
                ForeignKey(
                        entity = Tower::class,
                        parentColumns = ["tower_id"],
                        childColumns = ["tower_id"],
                        onDelete = CASCADE
                ),
                ForeignKey(
                        entity = Coordinate::class,
                        parentColumns = ["coord_id"],
                        childColumns = ["coord_id"],
                        onDelete = CASCADE
                )
        ],
        indices = [
                Index("add_id", unique = true),
                Index("tower_id"),
                Index("coord_id"),
                Index("type", "number", unique = true)
        ],
        tableName = DatabaseConst.ADDITIONAL_TABLE_NAME
)
data class Additional(
        @PrimaryKey(autoGenerate = true)
        val add_id: Long = 0,
        var tower_id: Long = 0,
        var coord_id: Long?,
        var changeDate: Date = Date(),

        var type: String?,
        var number: String,
)