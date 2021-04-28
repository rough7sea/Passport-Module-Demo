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

class DataManager(
    private val database: AppDatabase,
    private val searchLocationObjectManager: SearchLocationObjectManager<Any>
): IPassportManager<Any> {

    private val importFileManager by lazy { ImportFileManagerImpl(database) }
    private val exportFileManager by lazy { ExportFileManagerImpl(database) }
    private val objectBindingHandler by lazy { ObjectBindingHandlerImpl(database) }
    private val repositoryProvider by lazy { RepositoryProviderImpl(database) }

    companion object Builder{
        private lateinit var appDatabase: AppDatabase
        private lateinit var searchLocationObjectManager: SearchLocationObjectManager<Any>

        fun initialize(context: Context, locationManager: LocationManager) = apply {
            this.appDatabase = AppDatabase.initialize(context).build()
            this.searchLocationObjectManager = SearchLocationObjectManagerImpl(locationManager, context, appDatabase)
        }

        fun build() =
            if (::appDatabase.isInitialized && ::searchLocationObjectManager.isInitialized)
                DataManager(appDatabase, searchLocationObjectManager)
            else throw Exception("Location Manager or general Database module is not initialized")
    }


    override fun import(file: File): LiveData<WorkResult> = importFileManager.import(file)

    override fun import(files: List<File>): LiveData<WorkResult> = importFileManager.import(files)

    override fun <B : Any> export(bindingEntity: B, destinationPath: File): LiveData<WorkResult> =
        exportFileManager.export(bindingEntity, destinationPath)

    override fun findObjects(gpsLocation: Location, radius: Int): MutableLiveData<RequestResult<Any>> =
        searchLocationObjectManager.findObjects(gpsLocation, radius)

    override fun addListenerToNearestObjects(gpsLocation: Location, radius: Int, listener: (List<Any>) -> Unit) =
        searchLocationObjectManager.addListenerToNearestObjects(gpsLocation, radius, listener)

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


}