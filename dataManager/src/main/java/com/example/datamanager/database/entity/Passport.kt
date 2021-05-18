package com.example.datamanager.database.entity

import android.os.Parcelable
import androidx.room.*
import com.example.datamanager.database.DatabaseConst
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Main Passport entity object. Used to save/represent data into the system.
 *
 * @param passport_id primary entity id. Must be 0 before inserting new instant in DataBase.
 * @param changeDate Last time entity changed.
 */
@Entity(
        indices = [
                Index("passport_id", unique = true),
                Index("sectionName", unique = true),
        ],
        tableName = DatabaseConst.PASSPORT_TABLE_NAME
)
@Parcelize
data class Passport(
        @PrimaryKey(autoGenerate = true)
        val passport_id: Long = 0,
        var changeDate: Date = Date(),

        var sectionName: String,
        var siteId: Long?= null,
        var sectionId: String?= null,
        var echName: String?= null,
        var echkName: String?= null,
        var locationId: String?= null,
        var wayAmount: Int?= null,
        var currentWay: String?= null,
        var currentWayId: Long?= null,
        var initialMeter: Int?= null,
        var initialKm: Int?= null,
        var initialPk: Int?= null,
        var initialM: Int?= null,
        var plotLength: Int?= null,
        var suspensionAmount: Int? = null,
) : Parcelable
