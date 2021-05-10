package com.example.datamanager.database.repository

/**
 * Point providing uniform access to the repository API.
 */
interface RepositoryProvider {
    /**
     * Provide repository access.
     *
     * @return [Repository] by object class.
     */
    fun <T> getRepository(clazz: Class<T>) : Repository<T>
}