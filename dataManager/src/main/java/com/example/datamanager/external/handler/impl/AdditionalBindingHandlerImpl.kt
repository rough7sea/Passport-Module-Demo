package com.example.datamanager.external.handler.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.external.entities.LoadResult
import com.example.datamanager.external.handler.InternalObjectBindingHandler
import com.example.datamanager.utli.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdditionalBindingHandlerImpl(appDatabase: AppDatabase) : InternalObjectBindingHandler<Additional> {
    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())
    private var currentAdditionals: Map<Int, Long> = mutableMapOf()
    private var currentNumber: Int = -1

    private val result = MutableLiveData<LoadResult<Additional>>()

    override fun getActualInternalObject(): LiveData<LoadResult<Additional>> {
        CoroutineScope(Dispatchers.IO).launch {
            if (currentNumber == -1 || currentAdditionals.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException("Must set object binding before!")))
                return@launch
            }
            result.postValue(LoadResult.Loading())

            val additionalId = currentAdditionals[currentNumber]
            if (additionalId != null){
                val additional = additionalRepository.getById(additionalId)
                result.postValue(LoadResult.Success(additional!!))
            } else {
                result.postValue(LoadResult.Error(Exception("Internal Error")))
            }
        }
        return result
    }

    override fun setInternalObject(internalObject: Additional): LiveData<LoadResult<Additional>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading(internalObject))

            val additionals = additionalRepository.findAllByTowerId(internalObject.tower_id)

            var count = 0

            currentAdditionals = additionals
                .sortedBy { Integer.valueOf(Utils.clearNumber(it.number)) }
                .map { count++ to it.add_id }
                .toMap()

            currentNumber = currentAdditionals
                .filterValues { it == internalObject.add_id }.keys
                .toList()[0]

            result.postValue(LoadResult.Success(internalObject))
        }
        return result
    }

    override fun nextInternalObject(): LiveData<LoadResult<Additional>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading())
            if (currentNumber == -1 || currentAdditionals.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException("Must set object binding before!")))
                return@launch
            }
            if (currentNumber + 1 >= currentAdditionals.size){
                result.postValue(LoadResult.Error(RuntimeException("There are no more objects in this directions")))
                return@launch
            }
            getObjectByNumber(result, ++currentNumber)
        }
        return result
    }

    override fun previousInternalObject(): LiveData<LoadResult<Additional>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading())
            if (currentNumber == -1 || currentAdditionals.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException("Must set object binding before!")))
                return@launch
            }
            if (currentNumber - 1 < 0){
                result.postValue(LoadResult.Error(RuntimeException("There are no more objects in this directions")))
                return@launch
            }
            getObjectByNumber(result, --currentNumber)
        }
        return result
    }

    private fun getObjectByNumber(result: MutableLiveData<LoadResult<Additional>>, currentNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val additionalId = currentAdditionals[currentNumber]
            if (additionalId != null) {
                val additional = additionalRepository.getById(additionalId)
                result.postValue(LoadResult.Success(additional!!))
            } else {
                result.postValue(LoadResult.Error(Exception("Internal Error")))
            }
        }
    }
}