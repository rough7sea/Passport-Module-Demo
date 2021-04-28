package com.example.myapplication

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.exchange.impl.ExportFileManagerImpl
import com.example.myapplication.exchange.impl.ImportFileManagerImpl
import com.example.myapplication.external.entities.LoadResult
import com.example.myapplication.external.entities.RequestResult
import com.example.myapplication.external.entities.WorkResult
import com.example.myapplication.external.handler.impl.ObjectBindingHandlerImpl
import com.example.myapplication.search.impl.SearchLocationObjectManagerImpl
import java.io.File

class DataManager(
        private val database: AppDatabase,
        private val locationManager: LocationManager
): IPassportManager<Any> {

    private val importFileManager by lazy { ImportFileManagerImpl(database) }
    private val exportFileManager by lazy { ExportFileManagerImpl(database) }
    private val searchLocationObjectManager by lazy { SearchLocationObjectManagerImpl(locationManager) }
    private val objectBindingHandler by lazy { ObjectBindingHandlerImpl(database) }

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


}