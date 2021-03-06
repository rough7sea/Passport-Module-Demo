package com.example.datamanager.external.handler.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.external.entities.LoadResult
import com.example.datamanager.external.handler.InternalObjectBindingHandler
import com.example.datamanager.utli.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Implementation [InternalObjectBindingHandler] with [Tower] object.
 *
 * @param appDatabase Main application database.
 * @property currentTowers current [Tower]'s map with *<Number, Tower ID>* format.
 * @property currentNumber current number of selected object.
 * @property result main return [LiveData] object.
 */
class TowerBindingHandlerImpl(appDatabase: AppDatabase) : InternalObjectBindingHandler<Tower> {

    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private var currentTowers: Map<Int, Long> = mutableMapOf()
    private var currentNumber: Int = -1

    private val NO_TOWER_IN_DIRECTION_MESSAGE = "There are no more towers in this direction"
    private val SET_TOWER_BEFORE_MESSAGE = "Before work, you need to set the tower binding"

    private val result = MutableLiveData<LoadResult<Tower>>()

    override fun getActualInternalObject(): LiveData<LoadResult<Tower>>{
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(LoadResult.Loading())
            if (currentNumber == -1 || currentTowers.isEmpty()){
                result.postValue(LoadResult.Error(RuntimeException(SET_TOWER_BEFORE_MESSAGE)))
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
                result.postValue(LoadResult.Error(RuntimeException(SET_TOWER_BEFORE_MESSAGE)))
                return@launch
            }
            if (currentNumber + 1 >= currentTowers.size){
                result.postValue(LoadResult.Error(RuntimeException(NO_TOWER_IN_DIRECTION_MESSAGE)))
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
                result.postValue(LoadResult.Error(RuntimeException(SET_TOWER_BEFORE_MESSAGE)))
                return@launch
            }
            if (currentNumber - 1 < 0){
                result.postValue(LoadResult.Error(RuntimeException(NO_TOWER_IN_DIRECTION_MESSAGE)))
                return@launch
            }
            getObjectByNumber(result, --currentNumber)
        }
        return result
    }

    /**
     * Find tower in [currentTowers] map by number.
     *
     * @param result [LiveData] to collect result.
     * @param currentNumber tower object number to receive.
     */
    private fun getObjectByNumber(result: MutableLiveData<LoadResult<Tower>>, currentNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val towerId = currentTowers[currentNumber]
            if (towerId != null) {
                val tower = towerRepository.getById(towerId)
                if (tower != null){
                    result.postValue(LoadResult.Success(tower))
                } else {
                    Log.e("TOWER_HANDLER", "There are no Tower in system with id[$towerId]")
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