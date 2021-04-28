package com.example.datamanager.database.repository

interface RepositoryProvider {
    fun <T> getRepository(clazz: Class<T>) : Repository<T>
}