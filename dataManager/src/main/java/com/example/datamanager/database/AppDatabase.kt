package com.example.datamanager.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.datamanager.database.converter.Converter
import com.example.datamanager.database.dao.AdditionalDAO
import com.example.datamanager.database.dao.CoordinateDAO
import com.example.datamanager.database.dao.PassportDAO
import com.example.datamanager.database.dao.TowerDAO
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.entity.Tower

@Database(
    entities = [Tower::class, Passport::class, Additional::class, Coordinate::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun coordinateDao(): CoordinateDAO
    abstract fun towerDao(): TowerDAO
    abstract fun passportDao(): PassportDAO
    abstract fun additionalDao(): AdditionalDAO

    companion object Builder{
        @Volatile
        private lateinit var appDatabase: AppDatabase

        fun initialize(context: Context) = apply{
            appDatabase = buildDatabase(context)
        }

        fun build() =
            if (Builder::appDatabase.isInitialized){
                appDatabase
            } else {
                throw Exception("Databases is not initialized")
            }

        private fun buildDatabase(context: Context) : AppDatabase {
            synchronized(this){
                return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DatabaseConst.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}