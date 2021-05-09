package com.example.datamanager.database.entity

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.datamanager.database.DatabaseConst
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Main Additional entity object. Used to save/represent data into the system.
 *
 * @param add_id primary entity id. Must be 0 before inserting new instant in DataBase.
 * @param tower_id Entity have foreign key from [Tower] object via *tower_id*.
 * @param coord_id Entity have foreign key from [Coordinate] object via *coord_id*. Can be *null* if entity no coordinate.
 * @param changeDate Last time entity changed.
 */
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
@Parcelize
data class Additional(
        @PrimaryKey(autoGenerate = true)
        val add_id: Long = 0,
        var tower_id: Long = 0,
        var coord_id: Long?,
        var changeDate: Date = Date(),

        var type: String?,
        var number: String,
) : Parcelable