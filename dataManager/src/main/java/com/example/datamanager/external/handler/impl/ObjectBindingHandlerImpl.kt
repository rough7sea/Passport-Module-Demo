package com.example.datamanager.external.handler.impl

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

class ObjectBindingHandlerImpl(
    appDatabase: AppDatabase
) : ObjectBindingHandler{

    private val towerBindingHandlerImpl = TowerBindingHandlerImpl(appDatabase)
    private val additionalBindingHandlerImpl =  AdditionalBindingHandlerImpl(appDatabase)

    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())
    private val towerRepository = TowerRepository(appDatabase.towerDao())

    override fun <T> getActualObject(clazz: Class<T>): LiveData<LoadResult<T>> {
        return getHandler(clazz).getActualInternalObject()
    }

    override fun <T> setObjectBinding(objectBinding: T): MutableLiveData<LoadResult<T>> {
        val result = getHandler(objectBinding).setInternalObject(objectBinding)
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
        return result as MutableLiveData<LoadResult<T>>
    }

    override fun <T> nextObject(clazz: Class<T>): LiveData<LoadResult<T>> {
        return getHandler(clazz).nextInternalObject()
    }

    override fun <T> previousObject(clazz: Class<T>): LiveData<LoadResult<T>> {
        return getHandler(clazz).previousInternalObject()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getHandler(objectBinding: T): InternalObjectBindingHandler<T>{
        return when(objectBinding){
            is Tower -> towerBindingHandlerImpl as InternalObjectBindingHandler<T>
            is Additional -> additionalBindingHandlerImpl as InternalObjectBindingHandler<T>
            else -> throw RuntimeException("Invalid input object type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getHandler(clazz: Class<T>): InternalObjectBindingHandler<T>{
        return when(clazz){
            Tower::class.java -> towerBindingHandlerImpl as InternalObjectBindingHandler<T>
            Additional::class.java -> additionalBindingHandlerImpl as InternalObjectBindingHandler<T>
            else -> throw RuntimeException("Invalid input object type")
        }
    }
}