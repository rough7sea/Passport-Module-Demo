package com.example.datamanager.database.repository.impl

import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.database.repository.RepositoryProvider

class RepositoryProviderImpl(private val database: AppDatabase) : RepositoryProvider {

    @Suppress("UNCHECKED_CAST")
    override fun <T> getRepository(clazz: Class<T>): Repository<T> {
        return when(clazz) {
            Tower::class.java -> {
                TowerRepository(database.towerDao()) as Repository<T>
            }
            Passport::class.java -> {
                PassportRepository(database.passportDao()) as Repository<T>
            }
            Additional::class.java -> {
                AdditionalRepository(database.additionalDao()) as Repository<T>
            }
            Coordinate::class.java -> {
                CoordinateRepository(database.coordinateDao()) as Repository<T>
            }
            else -> throw RuntimeException("Invalid input class")
        }
    }

}