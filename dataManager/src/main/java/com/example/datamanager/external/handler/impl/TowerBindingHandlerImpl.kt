package com.example.datamanager.external.handler.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.external.entities.LoadResult
import com.example.datamanager.external.handler.InternalObjectBindingHandler
import com.example.datamanager.utli.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TowerBindingHandlerImpl(appDatabase: AppDatabase) : InternalObjectBindingHandler<Tower> {

    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private var currentTowers: Map<Int, Long> = mutableMapOf()
    private var currentNumber: Int = -1

    private val result = MutableLiveData<LoadResult<Tower>>()

    override fun getActualInternalObject(): LiveData<LoadResult<Tower>>{
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading())
            if (currentNumber == -1 || currentTowers.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException("Must set object binding before!")))
                return@launch
            }

            result.postValue(LoadResult.Loading())
            getObjectByNumber(result, currentNumber)
        }
        return result
    }

    override fun setInternalObject(internalObject: Tower): LiveData<LoadResult<Tower>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading(internalObject))

            val towers = towerRepository.findAllByPassportId(internalObject.passport_id)

            var count = 0

            currentTowers = towers
                    .sortedBy { Integer.valueOf(Utils.clearNumber(it.number)) }
                    .map { count++ to it.tower_id }
                    .toMap()

            currentNumber = currentTowers
                    .filterValues { it == internalObject.tower_id }.keys
                    .toList()[0]

            Log.i("TOWER_HANDLER", "Set tower to handler with id[${internalObject.tower_id}]")
            result.postValue(LoadResult.Success(internalObject))
        }
        return result
    }

    override fun nextInternalObject(): LiveData<LoadResult<Tower>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading())
            if (currentNumber == -1 || currentTowers.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException("Must set object binding before!")))
                return@launch
            }
            if (currentNumber + 1 >= currentTowers.size){
                result.postValue(LoadResult.Error(RuntimeException("There are no more objects in this directions")))
                return@launch
            }
            getObjectByNumber(result, ++currentNumber)
        }
        return result
    }

    override fun previousInternalObject(): LiveData<LoadResult<Tower>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading())
            if (currentNumber == -1 || currentTowers.isEmpty()){
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

    private fun getObjectByNumber(result: MutableLiveData<LoadResult<Tower>>, currentNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val towerId = currentTowers[currentNumber]
            if (towerId != null) {
                val tower = towerRepository.getById(towerId)
                if (tower != null){
                    result.postValue(LoadResult.Success(tower))
                } else {
                    result.postValue(LoadResult.Error(
                            RuntimeException("There are no Tower in system with id[$towerId]")))
                }
            } else {
                Log.e("TOWER_HANDLER", "Tower id can't be null")
                result.postValue(LoadResult.Error(Exception("Internal Error")))
            }
        }
    }
}