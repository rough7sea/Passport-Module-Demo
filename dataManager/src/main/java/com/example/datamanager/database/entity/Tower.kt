package com.example.datamanager.database.entity

import android.os.Parcelable
import androidx.room.*
import com.example.datamanager.database.DatabaseConst
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Main Tower entity object. Used to save/represent data into the system.
 *
 * @param tower_id primary entity id. Must be 0 before inserting new instant in DataBase.
 * @param passport_id Entity have foreign key from [Passport] object via *passport_id*.
 * @param coord_id Entity have foreign key from [Coordinate] object via *coord_id*. Can be *null* if entity no coordinate.
 * @param changeDate Last time entity changed.
 */
@Entity(
        foreignKeys = [
                ForeignKey(
                        entity = Passport::class,
                        parentColumns = ["passport_id"],
                        childColumns = ["passport_id"],
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = Coordinate::class,
                        parentColumns = ["coord_id"],
                        childColumns = ["coord_id"],
                        onDelete = ForeignKey.CASCADE
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
@Parcelize
data class Tower(
        @PrimaryKey(autoGenerate = true)
        val tower_id: Long = 0,
        var passport_id: Long = 0,
        var coord_id: Long? = null,
        var changeDate: Date = Date(),

        var idtf: String,
        var assetNum: String,
        var stopSeq: Int? = null,
        var km: Int? = null,
        var pk: Int? = null,
        var m: Int? = null,
        var tfType: String? = null,
        var turn: String? = null,
        var radius: String? = null,
        var number: String,
        var distance: Int? = null,
        var zigzag: Int? = null,
        var height: Int? = null,
        var offset: Int? = null,
        var grounded: Int? = null,
        var speed: Int? = null,
        var suspensionType: String? = null,
        var catenary: Int? = null,
        var wireType: String? = null,
        var countWire: String? = null,
        var gabarit: String? = null,
) : Parcelable