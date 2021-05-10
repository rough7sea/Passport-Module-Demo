package com.example.datamanager.exchange.impl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.datamanager.database.AppDatabase
import com.example.datamanager.database.converter.Converter
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.exchange.ImportFileManager
import com.example.datamanager.exchange.dto.FullSectionCertificate
import com.example.datamanager.exchange.dto.SectionCertificate
import com.example.datamanager.external.entities.WorkResult
import com.example.datamanager.utli.QueryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.core.ValueRequiredException
import java.io.File
import java.util.*

/**
 * Implementation [ImportFileManager].
 *
 * @param appDatabase Main application database.
 */
class ImportFileManagerImpl(appDatabase: AppDatabase)
    : ImportFileManager
{
    private val serializer = Persister()

    /**
     * Passport Repository.
     */
    private val passportRepository =  PassportRepository(appDatabase.passportDao())
    /**
     * Tower Repository.
     */
    private val towerRepository = TowerRepository(appDatabase.towerDao())
    /**
     * Coordinate Repository.
     */
    private val coordinateRepository = CoordinateRepository(appDatabase.coordinateDao())
    /**
     * Additional Repository.
     */
    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())

    val result = MutableLiveData<WorkResult>()

    override fun import(file: File) : LiveData<WorkResult>{
//        val mutableLiveData = MutableLiveData<WorkResult>()
        importXmlFromFile(file, result)
        return result
    }
    override fun import(files: List<File>) : LiveData<WorkResult> {

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

        val error = WorkResult.Error()

        CoroutineScope(Dispatchers.IO).launch {
            liveData.postValue(WorkResult.Progress(0))

            try {
                if (serializer.validate(FullSectionCertificate::class.java, file)) {
                    fullTowerImport(file, liveData, error)
                    Log.i("IMPORT","Import done for file $file")
                    return@launch
                }
            } catch (ex: ValueRequiredException){

            } catch (ex: Exception){
                Log.e("IMPORT", ex.localizedMessage, ex)
            }

            try {
                if (serializer.validate(SectionCertificate::class.java, file)){
                    passportImport(file, liveData, error)
                    Log.i("IMPORT","Import done for file $file")
                    return@launch
                }
            } catch (ex: Exception){
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

            val passport_id = passportRepository.add(Converter.fromXmlToPassport(sectionCertificate.passport!!))

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
                        var coord_id = coordinateRepository.add(
                            Coordinate(0, Date(), it.latitude!!, it.longitude!!))
                        if (coord_id == -1L){
                            coord_id = coordinateRepository
                                .getCoordinateByLongitudeAndLatitude(it.latitude!!, it.longitude!!)!!.coord_id
                        }
                        Converter.fromXmlToTower(it, coord_id)
                    } else {
                        Converter.fromXmlToTower(it, null)
                    }

                    tower.passport_id = passport_id
                    liveData.postValue(WorkResult.Progress(progress + step))
                    progress += step
                    tower
                }
            Log.i("IMPORT", "Import ${towers.size} towers")
            towerRepository.addAll(towers)
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

            var passport_id = passportRepository.add(Converter.fromXmlToPassport(fullTower.passport!!))
            if (passport_id == -1L){
                val currentPassport = passportRepository.findCurrentWithParameter(
                    QueryBuilder.buildSearchByAllFieldsQuery(fullTower.passport!!)
                ) ?: throw Exception("System error. Can't get saved passport")
                passport_id = currentPassport.passport_id
            }

            if (fullTower.towers.isEmpty()){
                throw RuntimeException("Tower from file{$file} is empty!")
            }

            val towersSize: Int = if (fullTower.towers.isNotEmpty()){
                fullTower.towers.size
            } else { 1 }
            val step = 100 / towersSize
            var progress = 0

            val towers = fullTower.towers
                .filter {
                    it.tower != null &&
                            it.tower!!.assetNum != null &&
                            it.tower!!.idtf != null &&
                            it.tower!!.number != null
                }.map { fullTower ->

                    val towerDto = fullTower.tower!!

                    val tower = if (towerDto.longitude != null && towerDto.latitude != null) {
                        var coord_id = coordinateRepository.add(
                            Coordinate(longitude = towerDto.longitude!!, latitude = towerDto.latitude!!)
                        )
                        if (coord_id == -1L) {
                            coord_id = coordinateRepository
                                .getCoordinateByLongitudeAndLatitude(towerDto.latitude!!, towerDto.longitude!!)!!.coord_id
                        }
                        Converter.fromXmlToTower(towerDto, coord_id)
                    } else {
                        Converter.fromXmlToTower(towerDto, null)
                    }

                    tower.passport_id = passport_id
                    val tower_id = towerRepository.add(tower)

                    val additionals = fullTower.additionals.map {
                        val additional = if (it.longitude != null && it.latitude != null) {
                            var coord_id = coordinateRepository.add(
                                Coordinate(longitude = it.longitude!!, latitude = it.latitude!!)
                            )
                            if (coord_id == -1L) {
                                coord_id = coordinateRepository
                                    .getCoordinateByLongitudeAndLatitude(it.latitude!!, it.longitude!!)!!.coord_id
                            }
                            Converter.fromXmlToAdditional(it, tower_id, coord_id)
                        } else {
                            Converter.fromXmlToAdditional(it, tower_id, null)
                        }

                        additional
                    }
                    Log.i("IMPORT", "Import ${additionals.size} additionals")
                    additionalRepository.addAll(additionals)

                    tower.passport_id = passport_id
                    liveData.postValue(WorkResult.Progress(progress + step))
                    progress += step
                    tower
                }
            Log.i("IMPORT", "Import ${towers.size} towers")
            liveData.postValue(WorkResult.Completed())
        } catch (ex: Exception){
            Log.e("IMPORT", ex.localizedMessage, ex)
            error.addError(ex)
            liveData.postValue(error)
        }
    }
}