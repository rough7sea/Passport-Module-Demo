package com.example.datamanager.exchange.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.converter.Converter
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.exchange.ExportFileManager
import com.example.datamanager.exchange.dto.FullSectionCertificate
import com.example.datamanager.exchange.dto.FullTower
import com.example.datamanager.external.entities.WorkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import org.simpleframework.xml.core.Persister
import java.io.File

class ExportFileManagerImpl(appDatabase: AppDatabase)
    : ExportFileManager
{
    private val serializer = Persister()

    private val passportRepository =  PassportRepository(appDatabase.passportDao())
    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private val coordinateRepository = CoordinateRepository(appDatabase.coordinateDao())
    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())


    override fun <B : Any> export(@NotNull bindingEntity: B,@NotNull destinationPath: File) : LiveData<WorkResult> {
        val result = MutableLiveData<WorkResult>()
        CoroutineScope(Dispatchers.IO).launch {
            val error = WorkResult.Error()

            result.postValue(WorkResult.Progress(0))
            try {
                Log.i("EXPORT", "export for entity ${bindingEntity::class.java.canonicalName} start")
                destinationPath.createNewFile()
                serializer.write(getFullSectionCertificate(bindingEntity), destinationPath)
                Log.i("EXPORT", "export for entity ${bindingEntity::class.java.canonicalName} successfully ended")
                result.postValue(WorkResult.Progress(100))
            } catch (ex: Exception){
                Log.e("EXPORT", ex.localizedMessage, ex)
                error.addError(ex)
            }

            if (error.errors.isNotEmpty()){
                result.postValue(error)
            } else {
                result.postValue(WorkResult.Completed())
            }
        }
        return result
    }

    private fun <B> getFullSectionCertificate(bindingEntity: B): FullSectionCertificate {
        val passport_id: Long = when(bindingEntity){

            is Tower -> bindingEntity.passport_id
            is Additional ->{
                val tower = towerRepository.getById(bindingEntity.tower_id)
                    ?: throw RuntimeException("Tower with id[${bindingEntity.tower_id}] can't be null")
                tower.passport_id
            }
            is Passport -> bindingEntity.passport_id

            else -> throw RuntimeException("Invalid input object type")
        }
        return createFullSectionCertificate(passport_id)
    }

    private fun createFullSectionCertificate(passport_id: Long): FullSectionCertificate{
        val passport = passportRepository.getById(passport_id)
            ?: throw RuntimeException("Passport with id{$passport_id} can not be null")
        val passportXml = Converter.fromPassportToXml(passport)

        val fullTowers = towerRepository.findAllByPassportId(passport_id).map {

            val coord = it.coord_id?.let { it1 -> coordinateRepository.getById(it1) }

            val towerXml = Converter.fromTowerToXml(it, coord)

            val additionalsXml = additionalRepository.findAllByTowerId(it.tower_id).map { add ->
                Converter.fromAdditionalToXml(add,
                    add.coord_id?.let { it1 -> coordinateRepository.getById(it1) })
            }
            FullTower(towerXml, additionalsXml)
        }
        return FullSectionCertificate(passportXml, fullTowers)
    }

}