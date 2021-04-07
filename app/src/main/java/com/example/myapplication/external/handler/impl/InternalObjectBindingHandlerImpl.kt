package com.example.myapplication.external.handler.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.repository.AdditionalRepository
import com.example.myapplication.external.entities.LoadResult
import com.example.myapplication.external.handler.InternalObjectBindingHandler
import com.example.myapplication.utli.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Tower -> Additional
class InternalObjectBindingHandlerImpl(appDatabase: AppDatabase) : InternalObjectBindingHandler<Additional>
{
    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())
    private var currentAdditionals: Map<Int, Long> = mutableMapOf()
    private var currentNumber: Int = -1

    private val result = MutableLiveData<LoadResult<Additional>>()
    fun getLiveDataResult() : MutableLiveData<LoadResult<Additional>> = result

    override fun getActualInternalObject(): LiveData<LoadResult<Additional>> {
        CoroutineScope(Dispatchers.IO).launch {
            if (currentNumber == -1 || currentAdditionals.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException("Must set object binding before!")))
                return@launch
            }
            result.postValue(LoadResult.Loading())

            val additionalId = currentAdditionals[currentNumber]
            if (additionalId != null){
                val additional = additionalRepository.findById(additionalId)
                result.postValue(LoadResult.Success(additional))
            } else {
                result.postValue(LoadResult.Error(Exception("Internal Error")))
            }
        }
        return result
    }

    override fun setInternalObject(additional: Additional): LiveData<LoadResult<Additional>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading(additional))

            val additionals = additionalRepository.findAllByTowerId(additional.tower_id)

            var count = 0

            currentAdditionals = additionals
                    .sortedBy { Integer.valueOf(Utils.clearNumber(it.number)) }
                    .map { count++ to it.add_id }
                    .toMap()

            currentNumber = currentAdditionals
                    .filterValues { it == additional.add_id }.keys
                    .toList()[0]

            result.postValue(LoadResult.Success(additional))
        }
        return result
    }

    override fun nextObject(): LiveData<LoadResult<Additional>> {
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

    override fun previousObject(): LiveData<LoadResult<Additional>> {
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
                val additional = additionalRepository.findById(additionalId)
                result.postValue(LoadResult.Success(additional))
            } else {
                result.postValue(LoadResult.Error(Exception("Internal Error")))
            }
        }
    }
}