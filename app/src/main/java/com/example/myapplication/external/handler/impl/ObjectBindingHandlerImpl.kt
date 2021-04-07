package com.example.myapplication.external.handler.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.database.repository.TowerRepository
import com.example.myapplication.external.entities.LoadResult
import com.example.myapplication.external.handler.ObjectBindingHandler
import com.example.myapplication.utli.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Passport -> Tower
class ObjectBindingHandlerImpl(appDatabase: AppDatabase) : ObjectBindingHandler<Tower>{

    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private var currentTowers: Map<Int, Long> = mutableMapOf()
    private var currentNumber: Int = -1

    private val result = MutableLiveData<LoadResult<Tower>>()

    fun getLiveDataResult() : LiveData<LoadResult<Tower>> = result


    override fun getActualObject(): LiveData<LoadResult<Tower>>{
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading())
            if (currentNumber == -1 || currentTowers.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException("Must set object binding before!")))
                return@launch
            }

            result.postValue(LoadResult.Loading())

            val towerId = currentTowers[currentNumber]
            if (towerId != null){
                val tower = towerRepository.findTowerById(towerId)
                result.postValue(LoadResult.Success(tower))
            } else {
                result.postValue(LoadResult.Error(Exception("Internal Error")))
            }
        }
        return result
    }

    override fun setObjectBinding(tower: Tower): LiveData<LoadResult<Tower>> {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading(tower))

            val towers = towerRepository.findAllByPassportId(tower.passport_id)

            var count = 0

            currentTowers = towers
                    .sortedBy { Integer.valueOf(Utils.clearNumber(it.number)) }
                    .map { count++ to it.tower_id }
                    .toMap()

            currentNumber = currentTowers
                    .filterValues { it == tower.tower_id }.keys
                    .toList()[0]

            result.postValue(LoadResult.Success(tower))
        }
        return result
    }

    override fun nextObject(): LiveData<LoadResult<Tower>> {
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

    override fun previousObject(): LiveData<LoadResult<Tower>> {
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
                val tower = towerRepository.findTowerById(towerId)
                result.postValue(LoadResult.Success(tower))
            } else {
                result.postValue(LoadResult.Error(Exception("Internal Error")))
            }
        }
    }
}