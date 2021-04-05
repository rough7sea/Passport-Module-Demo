package com.example.myapplication.exchange.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.converter.Converter
import com.example.myapplication.database.entity.Coordinate
import com.example.myapplication.database.entity.SectionCertificate
import com.example.myapplication.database.repository.CoordinateRepository
import com.example.myapplication.database.repository.PassportRepository
import com.example.myapplication.database.repository.TowerRepository
import com.example.myapplication.exchange.ImportFileManager
import com.example.myapplication.external.entities.WorkResult
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ImportFileManagerImpl(appDatabase: AppDatabase) : ImportFileManager {

    private val passportRepository =  PassportRepository(appDatabase.passportDao())
    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private val coordinateRepository = CoordinateRepository(appDatabase.coordinateDao())

    private val xmlMapper = XmlMapper()

    private val test = "<SectionCertificate><Header><siteId>1</siteId><sectionName>МОСКВА-ТОВАРНАЯ-ОКТЯБРЬСКАЯ 2 оп.24п-2п</sectionName><sectionId>0100102M.0002</sectionId><echName>ЭЧ-01 Московская</echName><echkName>ЭЧК-1 Останкино</echkName><locationId>STAN_1109</locationId><wayAmount>4</wayAmount><currentWay>2</currentWay><currentWayID>110000937823</currentWayID><CHANGEDATE>17.09.2019 04:09:37</CHANGEDATE><initialMeter>644601</initialMeter><initialKM>644</initialKM><initialPK>7</initialPK><initialM>1</initialM><plotLength>547</plotLength><suspensionAmount>2</suspensionAmount></Header><Towers><Tower><idtf>TF3192162</idtf><assetNum>A26622092</assetNum><stopSeq>1</stopSeq><km>644</km><pk>7</pk><m>1</m><TF_TYPE/><TURN>R</TURN><RADIUS>1060</RADIUS><number>24п</number><distance>43</distance><zigzag>-15</zigzag><height>600</height><offset>40</offset><Grounded>0</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>2222</longitude><latitude>532</latitude><Gabarit/></Tower><Tower><idtf>TF3192163</idtf><assetNum>A26622097</assetNum><stopSeq>2</stopSeq><km>644</km><pk>6</pk><m>58</m><TF_TYPE/><TURN>R</TURN><RADIUS>1060</RADIUS><number>22п</number><distance>52</distance><zigzag>-30</zigzag><height>600</height><offset>68</offset><Grounded>0</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192164</idtf><assetNum>A26622098</assetNum><stopSeq>3</stopSeq><km>644</km><pk>6</pk><m>6</m><TF_TYPE/><TURN>R</TURN><RADIUS>1060</RADIUS><number>20пС</number><distance>54</distance><zigzag>-36</zigzag><height>600</height><offset>180</offset><Grounded>0</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192165</idtf><assetNum>A26622094</assetNum><stopSeq>4</stopSeq><km>644</km><pk>5</pk><m>52</m><TF_TYPE/><TURN>R</TURN><RADIUS>1060</RADIUS><number>18п</number><distance>57</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>0</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192166</idtf><assetNum>A26622090</assetNum><stopSeq>5</stopSeq><km>644</km><pk>4</pk><m>95</m><TF_TYPE/><TURN/><RADIUS/><number>16п</number><distance>47</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192167</idtf><assetNum>A26622099</assetNum><stopSeq>6</stopSeq><km>644</km><pk>4</pk><m>48</m><TF_TYPE/><TURN>R</TURN><RADIUS>4000</RADIUS><number>14пС</number><distance>40</distance><zigzag>40</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192168</idtf><assetNum>A26622093</assetNum><stopSeq>7</stopSeq><km>644</km><pk>4</pk><m>8</m><TF_TYPE/><TURN>R</TURN><RADIUS>4000</RADIUS><number>12пС</number><distance>46</distance><zigzag>40</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192169</idtf><assetNum>A26622100</assetNum><stopSeq>8</stopSeq><km>644</km><pk>3</pk><m>62</m><TF_TYPE/><TURN>R</TURN><RADIUS>4000</RADIUS><number>10пС</number><distance>38</distance><zigzag>-40</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192170</idtf><assetNum>A26622091</assetNum><stopSeq>9</stopSeq><km>644</km><pk>3</pk><m>24</m><TF_TYPE>А</TF_TYPE><TURN/><RADIUS/><number>8п</number><distance>42</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192171</idtf><assetNum>A26622096</assetNum><stopSeq>10</stopSeq><km>644</km><pk>2</pk><m>82</m><TF_TYPE/><TURN/><RADIUS/><number>6пП</number><distance>43</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192172</idtf><assetNum>A26622095</assetNum><stopSeq>11</stopSeq><km>644</km><pk>2</pk><m>39</m><TF_TYPE/><TURN/><RADIUS/><number>4пП</number><distance>40</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3192173</idtf><assetNum>A26622089</assetNum><stopSeq>12</stopSeq><km>644</km><pk>1</pk><m>99</m><TF_TYPE/><TURN/><RADIUS/><number>2пП</number><distance>45</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-120</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>123</longitude><latitude>333</latitude><Gabarit/></Tower></Towers></SectionCertificate>"

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


            val filesCount = files.size
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

            val sectionCertificate: SectionCertificate

            try {
//                sectionCertificate = xmlMapper.readValue(file, SectionCertificate::class.java)
                sectionCertificate = xmlMapper.readValue(test, SectionCertificate::class.java)
            } catch (ex: Exception){
                error.addError(ex)
                return@launch
            }

            if (sectionCertificate.header == null){
                error.addError(RuntimeException("Passport for file{$file} is empty!"))
                liveData.postValue(error)
                return@launch
            }

            val passport_id = passportRepository.addPassport(Converter.fromXMLToPassport(sectionCertificate.header))

            val towersSize = sectionCertificate.towers.size
            val step = 100 / towersSize
            var progress = 0


            val towers = sectionCertificate.towers.filter {
                it.assetNum != null && it.idtf != null && it.number != null
            }.map {

                val tower = if (it.longitude != null && it.latitude != null){
                    var insert = coordinateRepository.addCoordinate(
                            Coordinate(longitude = it.longitude!!, latitude = it.latitude!!))
                    if (insert == -1L){
                        insert = coordinateRepository.getCoordinateByLongitudeAndLatitude(it.longitude!!, it.latitude!!)!!.coord_id
                    }
                    Converter.fromXMLTowerDtoToTower(it, insert)
                } else {
                    Converter.fromXMLTowerDtoToTower(it, null)
                }

                tower.passport_id = passport_id

                liveData.postValue(WorkResult.Progress(progress + step))
                progress += step

                tower
            }
            towerRepository.addAllTowers(towers)
            liveData.postValue(WorkResult.Completed())
        }
        return error
    }
}