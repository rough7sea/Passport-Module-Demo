package com.example.datamanager

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.repository.Repository
import com.example.datamanager.database.repository.impl.RepositoryProviderImpl
import com.example.datamanager.exchange.impl.ExportFileManagerImpl
import com.example.datamanager.exchange.impl.ImportFileManagerImpl
import com.example.datamanager.external.entities.LoadResult
import com.example.datamanager.external.entities.RequestResult
import com.example.datamanager.external.entities.WorkResult
import com.example.datamanager.external.handler.impl.ObjectBindingHandlerImpl
import com.example.datamanager.search.SearchLocationObjectManager
import com.example.datamanager.search.impl.SearchLocationObjectManagerImpl
import java.io.File

/**
 * Main API point for module.
 * Use the [DataManager.build] factory method to
 * create an instance of this fragment.
 *
 * @property database Main application database.
 * @property searchLocationObjectManager Search Location Manager.
 */
class DataManager(
    private val database: AppDatabase,
    private val searchLocationObjectManager: SearchLocationObjectManager<Any>
): IPassportManager<Any> {

    /**
     * Import File Manager.
     */
    private val importFileManager by lazy { ImportFileManagerImpl(database) }
    /**
     * Export File Manager.
     */
    private val exportFileManager by lazy { ExportFileManagerImpl(database) }
    /**
     * Object Binding Handler.
     */
    private val objectBindingHandler by lazy { ObjectBindingHandlerImpl(database) }
    /**
     * Repository Provider.
     */
    private val repositoryProvider by lazy { RepositoryProviderImpl(database) }

    /**
     * Main initializing point of DataManger.
     * - Require [LocationManager] to get GPS/location data.
     * - Require Application [Context] to get GPS/location permission.
     */
    companion object Builder{

        private lateinit var appDatabase: AppDatabase
        private lateinit var searchLocationObjectManager: SearchLocationObjectManager<Any>

        /**
         * Use this method to initialize a new instance of
         * this Data Manager using the provided parameters.
         *
         * @param [locationManager] require Location Manager to get GPS/location data.
         * @param context require Application Context to get GPS/location permission.
         */
        fun initialize(context: Context, locationManager: LocationManager) = apply {
            this.appDatabase = AppDatabase.initialize(context).build()
            this.searchLocationObjectManager = SearchLocationObjectManagerImpl(locationManager, context, appDatabase)
        }

        /**
         * Use this factory method to create a new instance of this Data Manager.
         */
        fun build() =
            if (::appDatabase.isInitialized && ::searchLocationObjectManager.isInitialized)
                DataManager(appDatabase, searchLocationObjectManager)
            else throw Exception("Location Manager or general Database module is not initialized")
    }


    override fun import(file: File): LiveData<WorkResult> = importFileManager.import(file)

    override fun import(files: List<File>): LiveData<WorkResult> = importFileManager.import(files)

    override fun <B : Any> export(bindingEntity: B, destinationPath: File): LiveData<WorkResult> =
        exportFileManager.export(bindingEntity, destinationPath)

    override fun getSearchResult(): MutableLiveData<RequestResult<Any>> =
        searchLocationObjectManager.getSearchResult()

    override fun findObjects(gpsLocation: Location, radius: Float): MutableLiveData<RequestResult<Any>> =
        searchLocationObjectManager.findObjects(gpsLocation, radius)

    override fun addListenerToNearestObjects(radius: Float, listener: (List<Any>) -> Unit) =
        searchLocationObjectManager.addListenerToNearestObjects(radius, listener)

    override fun <T> getActualObject(clazz: Class<T>): LiveData<LoadResult<T>> =
        objectBindingHandler.getActualObject(clazz)

    override fun <T> setObjectBinding(objectBinding: T): LiveData<LoadResult<T>> =
        objectBindingHandler.setObjectBinding(objectBinding)

    override fun <T> nextObject(clazz: Class<T>): LiveData<LoadResult<T>> =
        objectBindingHandler.nextObject(clazz)

    override fun <T> previousObject(clazz: Class<T>): LiveData<LoadResult<T>> =
        objectBindingHandler.previousObject(clazz)

    override fun <T> getRepository(clazz: Class<T>): Repository<T> =
        repositoryProvider.getRepository(clazz)

    override fun getImportResult(): LiveData<WorkResult> =
        importFileManager.getImportResult()

    override fun getExportResult(): LiveData<WorkResult> =
        exportFileManager.getExportResult()

}