package com.example.datamanager.external.handler.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.external.entities.LoadResult
import com.example.datamanager.external.handler.InternalObjectBindingHandler
import com.example.datamanager.external.handler.ObjectBindingHandler
import com.example.datamanager.utli.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Implementation [ObjectBindingHandler]. Can handle [Tower] and [Additional] objects.
 *
 * @param appDatabase Main application database.
 * @property towerBindingHandler [Tower] binding handler.
 * @property additionalBindingHandler [Additional] binding handler.
 */
class ObjectBindingHandlerImpl(
    appDatabase: AppDatabase
) : ObjectBindingHandler{

    private val towerBindingHandler = TowerBindingHandlerImpl(appDatabase)
    private val additionalBindingHandler =  AdditionalBindingHandlerImpl(appDatabase)

    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())
    private val towerRepository = TowerRepository(appDatabase.towerDao())

    override fun <T> getActualObject(clazz: Class<T>): LiveData<LoadResult<T>> {
        return getHandler(clazz).getActualInternalObject()
    }

    override fun <T> setObjectBinding(objectBinding: T): MutableLiveData<LoadResult<T>> {
        val result = getHandler(objectBinding).setInternalObject(objectBinding)
        CoroutineScope(Dispatchers.IO).launch {
            when(objectBinding){
                is Tower -> {
                    val additionals = additionalRepository.findAllByTowerId(objectBinding.tower_id)
                    if (additionals.isNotEmpty()){
                        getHandler(Additional::class.java).setInternalObject(
                                additionals.sortedBy { Integer.valueOf(Utils.clearNumber(it.number)) }[0])
                    }
                }
                is Additional -> {
                    val tower = towerRepository.getById(objectBinding.tower_id)
                            ?: throw RuntimeException("Tower with id[${objectBinding.tower_id}] can't be null")
                    getHandler(Tower::class.java).setInternalObject(tower)
                }
            }
        }
        return result as MutableLiveData<LoadResult<T>>
    }

    override fun <T> nextObject(clazz: Class<T>): LiveData<LoadResult<T>> {
        return getHandler(clazz).nextInternalObject()
    }

    override fun <T> previousObject(clazz: Class<T>): LiveData<LoadResult<T>> {
        return getHandler(clazz).previousInternalObject()
    }

    /**
     * Handler receive method.
     *
     * @param objectBinding system object.
     * @return Object handler via object type.
     * @throws [RuntimeException] if object type is wrong.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> getHandler(objectBinding: T): InternalObjectBindingHandler<T>{
        return when(objectBinding){
            is Tower -> towerBindingHandler as InternalObjectBindingHandler<T>
            is Additional -> additionalBindingHandler as InternalObjectBindingHandler<T>
            else -> {
                Log.e("OBJECT_HANDLER", "Invalid input object type [$objectBinding]")
                throw RuntimeException("Invalid input object type")
            }
        }
    }

    /**
     * Handler receive method.
     *
     * @param clazz system object class.
     * @return Object handler via object class.
     * @throws [RuntimeException] if object class is wrong.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> getHandler(clazz: Class<T>): InternalObjectBindingHandler<T>{
        return when(clazz){
            Tower::class.java -> towerBindingHandler as InternalObjectBindingHandler<T>
            Additional::class.java -> additionalBindingHandler as InternalObjectBindingHandler<T>
            else ->{
                Log.e("OBJECT_HANDLER", "Invalid input object class [$clazz]")
                throw RuntimeException("Invalid input object class")
            }
        }
    }
}