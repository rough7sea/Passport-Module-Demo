package com.example.datamanager.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.datamanager.database.DatabaseConst
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Main Coordinate entity object. Used to save/represent data into the system.
 *
 * @param coord_id primary entity id. Must be 0 before inserting new instant in DataBase.
 * @param changeDate Last time entity changed.
 */
@Entity(
        indices = [
                Index(value = ["coord_id"], unique = true),
                Index(value = ["longitude", "latitude"], unique = true)
        ],
        tableName = DatabaseConst.COORDINATE_TABLE_NAME
)
@Parcelize
data class Coordinate(
        @PrimaryKey(autoGenerate = true)
        val coord_id: Long = 0,
        var changeDate: Date = Date(),

        var latitude: Double,
        var longitude: Double,
) : Parcelable