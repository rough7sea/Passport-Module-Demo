package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.converter.Converter
import com.example.myapplication.dao.AdditionalDAO
import com.example.myapplication.dao.CoordinateDAO
import com.example.myapplication.dao.PassportDAO
import com.example.myapplication.dao.TowerDAO
import com.example.myapplication.entity.Additional
import com.example.myapplication.entity.Coordinate
import com.example.myapplication.entity.Passport
import com.example.myapplication.entity.Tower

@Database(
        entities = [Tower::class, Passport::class, Additional::class, Coordinate::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coordinateDao(): CoordinateDAO
    abstract fun towerDao(): TowerDAO
    abstract fun passportDao(): PassportDAO
    abstract fun additionalDao(): AdditionalDAO
}