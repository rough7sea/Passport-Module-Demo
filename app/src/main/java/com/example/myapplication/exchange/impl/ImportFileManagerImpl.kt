package com.example.myapplication.exchange.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.converter.Converter
import com.example.myapplication.database.entity.Coordinate
import com.example.myapplication.database.repository.AdditionalRepository
import com.example.myapplication.database.repository.CoordinateRepository
import com.example.myapplication.database.repository.PassportRepository
import com.example.myapplication.database.repository.TowerRepository
import com.example.myapplication.exchange.ImportFileManager
import com.example.myapplication.exchange.dto.FullSectionCertificate
import com.example.myapplication.exchange.dto.SectionCertificate
import com.example.myapplication.external.entities.WorkResult
import com.example.myapplication.utli.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.core.ValueRequiredException
import java.io.File


class ImportFileManagerImpl(appDatabase: AppDatabase)
    : ImportFileManager
{
    private val serializer = Persister()

    private val passportRepository =  PassportRepository(appDatabase.passportDao())
    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private val coordinateRepository = CoordinateRepository(appDatabase.coordinateDao())
    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())

    override fun import(file: File) : LiveData<WorkResult>{
        val mutableLiveData = MutableLiveData<WorkResult>()
        importXmlFromFile(file, mutableLiveData)
        return mutableLiveData
    }

    override fun import(files: List<File>) : LiveData<WorkResult> {
        val result = MutableLiveData<WorkResult>()

        CoroutineScope(Dispatchers.IO).launch {
            val exportErrors: MutableList<Exception> = mutableListOf()

            result.postValue(WorkResult.Progress(0))

            val filesCount = if (files.isNotEmpty()){
                files.size
            } else { 1 }
            val step = 100 / filesCount
            var progress = 0

            files.forEach {

                val error = importXmlFromFile(it, MutableLiveData<WorkResult>())
                if (error.errors.isNotEmpty()){
                    exportErrors.addAll(error.errors)
                }

                result.postValue(WorkResult.Progress(progress + step))
                progress += step
            }

            val completed = WorkResult.Completed()
            completed.errors.addAll(exportErrors)
        }

        return result
    }

    private fun importXmlFromFile(file : File, liveData: MutableLiveData<WorkResult>) : WorkResult{

        liveData.postValue(WorkResult.Progress(0))

        val error = WorkResult.Error()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (serializer.validate(FullSectionCertificate::class.java, file)) {
                    fullTowerImport(file, liveData, error)
                    Log.i("IMPORT","Import done for file $file")
                    return@launch
                }
            }catch (ex: ValueRequiredException){

            } catch (ex: Exception){
                Log.e("IMPORT", ex.localizedMessage, ex)
            }
            try {
                if (serializer.validate(SectionCertificate::class.java, file)){
                    passportImport(file, liveData, error)
                    Log.i("IMPORT","Import done for file $file")
                    return@launch
                }
            }catch (ex: Exception){
                Log.e("IMPORT", ex.localizedMessage, ex)
            }
            Log.e("IMPORT","Wrong file type $file")
            throw RuntimeException("Wrong file type $file")
        }
        return error
    }

    private fun passportImport(file : File, liveData: MutableLiveData<WorkResult>,
                               error : WorkResult.Error) {
        try {
            val sectionCertificate = serializer.read(SectionCertificate::class.java, file)

            if (sectionCertificate.passport == null){
                throw RuntimeException("Passport from file{$file} is empty!")
            }

            val passport_id = passportRepository.addPassport(Converter.fromXmlToPassport(sectionCertificate.passport!!))

            val towersSize: Int = if (sectionCertificate.towers.isNotEmpty()){
                sectionCertificate.towers.size
            } else { 1 }

            val step = 100 / towersSize
            var progress = 0


            val towers = sectionCertificate.towers
                    .filter {
                        it.assetNum != null && it.idtf != null && it.number != null
                    }.map {
                        val tower = if (it.longitude != null && it.latitude != null){
                            var insert = coordinateRepository.addCoordinate(
                                    Coordinate(longitude = it.longitude!!, latitude = it.latitude!!))
                            if (insert == -1L){
                                insert = coordinateRepository.getCoordinateByLongitudeAndLatitude(it.longitude!!, it.latitude!!)!!.coord_id
                            }
                            Converter.fromXmlToTower(it, insert)
                        } else {
                            Converter.fromXmlToTower(it, null)
                        }

                        tower.passport_id = passport_id
                        liveData.postValue(WorkResult.Progress(progress + step))
                        progress += step
                        tower
                    }
            towerRepository.addAllTowers(towers)
            liveData.postValue(WorkResult.Completed())
        } catch (ex: Exception){
            Log.e("IMPORT", ex.localizedMessage, ex)
            error.addError(ex)
            liveData.postValue(error)
        }

    }

    private fun fullTowerImport(file : File, liveData: MutableLiveData<WorkResult>, error : WorkResult.Error) {
        try {
            val fullTower = serializer.read(FullSectionCertificate::class.java, file)

            if (fullTower.passport == null){
                throw RuntimeException("Passport from file{$file} is empty!")
            }

            var passport_id = passportRepository.addPassport(Converter.fromXmlToPassport(fullTower.passport!!))
            if (passport_id == -1L){
                passport_id = passportRepository.findPassportWithParameters(QueryBuilder.buildSearchByAllFieldsQuery(fullTower.passport!!)).passport_id
            }

            if (fullTower.towers.isEmpty()){
                throw RuntimeException("Tower from file{$file} is empty!")
            }


            val towersSize: Int = if (fullTower.towers.isNotEmpty()){
                fullTower.towers.size
            } else { 1 }
            val step = 100 / towersSize
            var progress = 0

            fullTower.towers
                    .filter {
                        it.tower != null &&
                                it.tower!!.assetNum != null &&
                                it.tower!!.idtf != null &&
                                it.tower!!.number != null
                    }.map { fullTower ->

                        val towerDto = fullTower.tower!!

                        val tower = if (towerDto.longitude != null && towerDto.latitude != null) {
                            var insert = coordinateRepository.addCoordinate(
                                    Coordinate(
                                            longitude = towerDto.longitude!!,
                                            latitude = towerDto.latitude!!
                                    )
                            )
                            if (insert == -1L) {
                                insert = coordinateRepository.getCoordinateByLongitudeAndLatitude(
                                        towerDto.longitude!!,
                                        towerDto.latitude!!
                                )!!.coord_id
                            }
                            Converter.fromXmlToTower(towerDto, insert)
                        } else {
                            Converter.fromXmlToTower(towerDto, null)
                        }

                        tower.passport_id = passport_id
                        val tower_id = towerRepository.addTower(tower)

                        val additionals = fullTower.additionals.map {
                            val additional = if (it.longitude != null && it.latitude != null) {
                                var coord = coordinateRepository.addCoordinate(
                                        Coordinate(longitude = it.longitude!!, latitude = it.latitude!!)
                                )
                                if (coord == -1L) {
                                    coord = coordinateRepository.getCoordinateByLongitudeAndLatitude(
                                            it.longitude!!,
                                            it.latitude!!
                                    )!!.coord_id
                                }
                                Converter.fromXmlToAdditional(it, tower_id, coord)
                            } else {
                                Converter.fromXmlToAdditional(it, tower_id, null)
                            }

                            additional
                        }
                        additionalRepository.addAllAdditionals(additionals)

                        tower.passport_id = passport_id
                        liveData.postValue(WorkResult.Progress(progress + step))
                        progress += step
                        tower
                    }
            liveData.postValue(WorkResult.Completed())
        } catch (ex: Exception){
            Log.e("IMPORT", ex.localizedMessage, ex)
            error.addError(ex)
            liveData.postValue(error)
        }
    }
}