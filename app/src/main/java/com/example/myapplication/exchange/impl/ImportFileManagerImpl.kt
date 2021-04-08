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

    private val test = "<SectionCertificate><Header><siteId>1</siteId><sectionName>МОСКВА-ТОВАРНАЯ-ОКТЯБРЬСКАЯ 1 оп.1п-43п</sectionName><sectionId>0100101M.001</sectionId><echName>ЭЧ-01 Московская</echName><echkName>ЭЧК-1 Останкино</echkName><locationId>STAN_1109</locationId><wayAmount>4</wayAmount><currentWay>1</currentWay><currentWayID>110000903273</currentWayID><CHANGEDATE>03.02.2020 11:02:30</CHANGEDATE><initialMeter>644381</initialMeter><initialKM>644</initialKM><initialPK>4</initialPK><initialM>81</initialM><plotLength>1089</plotLength><suspensionAmount>2</suspensionAmount></Header><Towers><Tower><idtf>TF3093466</idtf><assetNum>A26622070</assetNum><stopSeq>1</stopSeq><km>644</km><pk>4</pk><m>81</m><TF_TYPE/><TURN>R</TURN><RADIUS>4300</RADIUS><number>1п</number><distance>52</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>123</longitude><latitude>333</latitude><Gabarit/></Tower><Tower><idtf>TF3093467</idtf><assetNum>A26622069</assetNum><stopSeq>2</stopSeq><km>644</km><pk>5</pk><m>33</m><TF_TYPE/><TURN>R</TURN><RADIUS>4300</RADIUS><number>3пП</number><distance>47</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>121243</longitude><latitude>335233</latitude><Gabarit/></Tower><Tower><idtf>TF3093468</idtf><assetNum>A26622068</assetNum><stopSeq>3</stopSeq><km>644</km><pk>5</pk><m>80</m><TF_TYPE/><TURN>R</TURN><RADIUS>4300</RADIUS><number>5пП</number><distance>40</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>634123</longitude><latitude>6346333</latitude><Gabarit/></Tower><Tower><idtf>TF3093469</idtf><assetNum>A26622077</assetNum><stopSeq>4</stopSeq><km>644</km><pk>6</pk><m>20</m><TF_TYPE/><TURN/><RADIUS/><number>7пП</number><distance>52</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093470</idtf><assetNum>A26622082</assetNum><stopSeq>5</stopSeq><km>644</km><pk>6</pk><m>72</m><TF_TYPE/><TURN/><RADIUS/><number>9п</number><distance>42</distance><zigzag>15</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093471</idtf><assetNum>A26622087</assetNum><stopSeq>6</stopSeq><km>644</km><pk>7</pk><m>14</m><TF_TYPE/><TURN/><RADIUS/><number>11п</number><distance>55</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093472</idtf><assetNum>A26622084</assetNum><stopSeq>7</stopSeq><km>644</km><pk>7</pk><m>69</m><TF_TYPE/><TURN/><RADIUS/><number>13п</number><distance>60</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093473</idtf><assetNum>A26622067</assetNum><stopSeq>8</stopSeq><km>644</km><pk>8</pk><m>29</m><TF_TYPE/><TURN/><RADIUS/><number>15п</number><distance>64</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093474</idtf><assetNum>A26622071</assetNum><stopSeq>9</stopSeq><km>644</km><pk>8</pk><m>93</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>17п</number><distance>61</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093475</idtf><assetNum>A26622083</assetNum><stopSeq>10</stopSeq><km>644</km><pk>9</pk><m>54</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>19п</number><distance>56</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093476</idtf><assetNum>A26622075</assetNum><stopSeq>11</stopSeq><km>644</km><pk>10</pk><m>10</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>21п</number><distance>48</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093477</idtf><assetNum>A26622086</assetNum><stopSeq>12</stopSeq><km>644</km><pk>10</pk><m>58</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>23п</number><distance>41</distance><zigzag>15</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093478</idtf><assetNum>A26622074</assetNum><stopSeq>13</stopSeq><km>644</km><pk>10</pk><m>99</m><TF_TYPE/><TURN/><RADIUS/><number>25п</number><distance>43</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093479</idtf><assetNum>A26622078</assetNum><stopSeq>14</stopSeq><km>645</km><pk>1</pk><m>42</m><TF_TYPE/><TURN/><RADIUS/><number>27п</number><distance>48</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093480</idtf><assetNum>A26622073</assetNum><stopSeq>15</stopSeq><km>645</km><pk>1</pk><m>90</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>29пС</number><distance>35</distance><zigzag>-40</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093481</idtf><assetNum>A26622076</assetNum><stopSeq>16</stopSeq><km>645</km><pk>2</pk><m>25</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>31п</number><distance>41</distance><zigzag>15</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093482</idtf><assetNum>A26622088</assetNum><stopSeq>17</stopSeq><km>645</km><pk>2</pk><m>66</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>33п</number><distance>47</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093483</idtf><assetNum>A26622085</assetNum><stopSeq>18</stopSeq><km>645</km><pk>3</pk><m>13</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>35пС</number><distance>58</distance><zigzag>-40</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093484</idtf><assetNum>A26622081</assetNum><stopSeq>19</stopSeq><km>645</km><pk>3</pk><m>71</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>37п</number><distance>54</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093485</idtf><assetNum>A26622072</assetNum><stopSeq>20</stopSeq><km>645</km><pk>4</pk><m>25</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>39п</number><distance>52</distance><zigzag>-30</zigzag><height>600</height><offset>150</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093486</idtf><assetNum>A26622080</assetNum><stopSeq>21</stopSeq><km>645</km><pk>4</pk><m>77</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>41п</number><distance>44</distance><zigzag>-15</zigzag><height>600</height><offset>200</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093487</idtf><assetNum>A26622079</assetNum><stopSeq>22</stopSeq><km>645</km><pk>5</pk><m>21</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>43п</number><distance>49</distance><zigzag>-30</zigzag><height>600</height><offset>200</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower></Towers></SectionCertificate>"

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