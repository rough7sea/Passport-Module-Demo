package com.example.myapplication.exchange.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.converter.Converter
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.entity.Passport
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.database.repository.AdditionalRepository
import com.example.myapplication.database.repository.CoordinateRepository
import com.example.myapplication.database.repository.PassportRepository
import com.example.myapplication.database.repository.TowerRepository
import com.example.myapplication.exchange.ExportFileManager
import com.example.myapplication.exchange.dto.FullSectionCertificate
import com.example.myapplication.exchange.dto.FullTower
import com.example.myapplication.external.entities.WorkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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


    override fun <B> export(bindingEntity: B, destinationPath: File) : LiveData<WorkResult> {
        val result = MutableLiveData<WorkResult>()
        CoroutineScope(Dispatchers.IO).launch {
            val error = WorkResult.Error()

            result.postValue(WorkResult.Progress(0))
            try {
                Log.i("EXPORT", "export for entity ${bindingEntity!!::class.java.canonicalName} start")
                destinationPath.createNewFile()
                serializer.write(getFullSectionCertificate(bindingEntity), destinationPath)
                Log.i("EXPORT", "export for entity ${bindingEntity!!::class.java.canonicalName} successfully ended")
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
            is Additional -> towerRepository.findTowerById(bindingEntity.tower_id).passport_id
            is Passport -> bindingEntity.passport_id

            else -> throw RuntimeException("Invalid input object type")
        }
        return createFullSectionCertificate(passport_id)
    }

    private fun createFullSectionCertificate(passport_id: Long): FullSectionCertificate{
        val passport = passportRepository.findPassportById(passport_id)
        val passportXml = Converter.fromPassportToXml(passport)

        val fullTowers = towerRepository.findAllByPassportId(passport_id).map {

            val coord = it.coord_id?.let { it1 -> coordinateRepository.getCoordinateById(it1) }

            val towerXml = Converter.fromTowerToXml(it, coord)

            val additionalsXml = additionalRepository.findAllByTowerId(it.tower_id).map { add ->
                Converter.fromAdditionalToXml(add, add.coord_id?.let { it1 -> coordinateRepository.getCoordinateById(it1) })
            }
            FullTower(towerXml, additionalsXml)
        }
        return FullSectionCertificate(passportXml, fullTowers)
    }

}