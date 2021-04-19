package com.example.myapplication.exchange.impl

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
import java.io.File


class ImportFileManagerImpl(appDatabase: AppDatabase)
    : ImportFileManager
{
    private val serializer = Persister()

    private val passportRepository =  PassportRepository(appDatabase.passportDao())
    private val towerRepository = TowerRepository(appDatabase.towerDao())
    private val coordinateRepository = CoordinateRepository(appDatabase.coordinateDao())
    private val additionalRepository = AdditionalRepository(appDatabase.additionalDao())

    private val testPassport = "<SectionCertificate><Header><siteId>1</siteId><sectionName>МОСКВА-ТОВАРНАЯ-ОКТЯБРЬСКАЯ 1 оп.1п-43п</sectionName><sectionId>0100101M.001</sectionId><echName>ЭЧ-01 Московская</echName><echkName>ЭЧК-1 Останкино</echkName><locationId>STAN_1109</locationId><wayAmount>4</wayAmount><currentWay>1</currentWay><currentWayID>110000903273</currentWayID><CHANGEDATE>03.02.2020 11:02:30</CHANGEDATE><initialMeter>644381</initialMeter><initialKM>644</initialKM><initialPK>4</initialPK><initialM>81</initialM><plotLength>1089</plotLength><suspensionAmount>2</suspensionAmount></Header><Towers><Tower><idtf>TF3093466</idtf><assetNum>A26622070</assetNum><stopSeq>1</stopSeq><km>644</km><pk>4</pk><m>81</m><TF_TYPE/><TURN>R</TURN><RADIUS>4300</RADIUS><number>1п</number><distance>52</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>123</longitude><latitude>333</latitude><Gabarit/></Tower><Tower><idtf>TF3093467</idtf><assetNum>A26622069</assetNum><stopSeq>2</stopSeq><km>644</km><pk>5</pk><m>33</m><TF_TYPE/><TURN>R</TURN><RADIUS>4300</RADIUS><number>3пП</number><distance>47</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>121243</longitude><latitude>335233</latitude><Gabarit/></Tower><Tower><idtf>TF3093468</idtf><assetNum>A26622068</assetNum><stopSeq>3</stopSeq><km>644</km><pk>5</pk><m>80</m><TF_TYPE/><TURN>R</TURN><RADIUS>4300</RADIUS><number>5пП</number><distance>40</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude>634123</longitude><latitude>6346333</latitude><Gabarit/></Tower><Tower><idtf>TF3093469</idtf><assetNum>A26622077</assetNum><stopSeq>4</stopSeq><km>644</km><pk>6</pk><m>20</m><TF_TYPE/><TURN/><RADIUS/><number>7пП</number><distance>52</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093470</idtf><assetNum>A26622082</assetNum><stopSeq>5</stopSeq><km>644</km><pk>6</pk><m>72</m><TF_TYPE/><TURN/><RADIUS/><number>9п</number><distance>42</distance><zigzag>15</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093471</idtf><assetNum>A26622087</assetNum><stopSeq>6</stopSeq><km>644</km><pk>7</pk><m>14</m><TF_TYPE/><TURN/><RADIUS/><number>11п</number><distance>55</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093472</idtf><assetNum>A26622084</assetNum><stopSeq>7</stopSeq><km>644</km><pk>7</pk><m>69</m><TF_TYPE/><TURN/><RADIUS/><number>13п</number><distance>60</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093473</idtf><assetNum>A26622067</assetNum><stopSeq>8</stopSeq><km>644</km><pk>8</pk><m>29</m><TF_TYPE/><TURN/><RADIUS/><number>15п</number><distance>64</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093474</idtf><assetNum>A26622071</assetNum><stopSeq>9</stopSeq><km>644</km><pk>8</pk><m>93</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>17п</number><distance>61</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093475</idtf><assetNum>A26622083</assetNum><stopSeq>10</stopSeq><km>644</km><pk>9</pk><m>54</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>19п</number><distance>56</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093476</idtf><assetNum>A26622075</assetNum><stopSeq>11</stopSeq><km>644</km><pk>10</pk><m>10</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>21п</number><distance>48</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093477</idtf><assetNum>A26622086</assetNum><stopSeq>12</stopSeq><km>644</km><pk>10</pk><m>58</m><TF_TYPE/><TURN>R</TURN><RADIUS>4260</RADIUS><number>23п</number><distance>41</distance><zigzag>15</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093478</idtf><assetNum>A26622074</assetNum><stopSeq>13</stopSeq><km>644</km><pk>10</pk><m>99</m><TF_TYPE/><TURN/><RADIUS/><number>25п</number><distance>43</distance><zigzag>-30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093479</idtf><assetNum>A26622078</assetNum><stopSeq>14</stopSeq><km>645</km><pk>1</pk><m>42</m><TF_TYPE/><TURN/><RADIUS/><number>27п</number><distance>48</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093480</idtf><assetNum>A26622073</assetNum><stopSeq>15</stopSeq><km>645</km><pk>1</pk><m>90</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>29пС</number><distance>35</distance><zigzag>-40</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093481</idtf><assetNum>A26622076</assetNum><stopSeq>16</stopSeq><km>645</km><pk>2</pk><m>25</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>31п</number><distance>41</distance><zigzag>15</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093482</idtf><assetNum>A26622088</assetNum><stopSeq>17</stopSeq><km>645</km><pk>2</pk><m>66</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>33п</number><distance>47</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093483</idtf><assetNum>A26622085</assetNum><stopSeq>18</stopSeq><km>645</km><pk>3</pk><m>13</m><TF_TYPE/><TURN>R</TURN><RADIUS>1570</RADIUS><number>35пС</number><distance>58</distance><zigzag>-40</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093484</idtf><assetNum>A26622081</assetNum><stopSeq>19</stopSeq><km>645</km><pk>3</pk><m>71</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>37п</number><distance>54</distance><zigzag>30</zigzag><height>600</height><offset>0</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093485</idtf><assetNum>A26622072</assetNum><stopSeq>20</stopSeq><km>645</km><pk>4</pk><m>25</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>39п</number><distance>52</distance><zigzag>-30</zigzag><height>600</height><offset>150</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093486</idtf><assetNum>A26622080</assetNum><stopSeq>21</stopSeq><km>645</km><pk>4</pk><m>77</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>41п</number><distance>44</distance><zigzag>-15</zigzag><height>600</height><offset>200</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower><Tower><idtf>TF3093487</idtf><assetNum>A26622079</assetNum><stopSeq>22</stopSeq><km>645</km><pk>5</pk><m>21</m><TF_TYPE/><TURN>R</TURN><RADIUS>2170</RADIUS><number>43п</number><distance>49</distance><zigzag>-30</zigzag><height>600</height><offset>200</offset><Grounded>1</Grounded><SPEED>120</SPEED><suspensionType>КС-160</suspensionType><catenary>66</catenary><WireType>МФ-100</WireType><CountWire>2</CountWire><longitude/><latitude/><Gabarit/></Tower></Towers></SectionCertificate>"
    private val test2 = "<SectionCertificate><Header><CHANGEDATE>03.02.2020 11:02:30</CHANGEDATE><currentWay>1</currentWay><currentWayID>110000903273</currentWayID><echName>ЭЧ-01 Московская</echName><echkName>ЭЧК-1 Останкино</echkName><initialKM>644</initialKM><initialM>81</initialM><initialMeter>644381</initialMeter><initialPK>4</initialPK><locationId>STAN_1109</locationId><plotLength>1089</plotLength><sectionId>0100101M.001</sectionId><sectionName>МОСКВА-ТОВАРНАЯ-ОКТЯБРЬСКАЯ 1 оп.1п-43п</sectionName><siteId>1</siteId><suspensionAmount>2</suspensionAmount><wayAmount>4</wayAmount></Header><Towers><FullTower><Additionals/><Tower><assetNum>A26622070</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>52</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093466</idtf><km>644</km><latitude>333.0</latitude><longitude>123.0</longitude><m>81</m><number>1п</number><offset>0</offset><pk>4</pk><RADIUS>4300</RADIUS><SPEED>120</SPEED><stopSeq>1</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622069</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>47</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093467</idtf><km>644</km><latitude>335233.0</latitude><longitude>121243.0</longitude><m>33</m><number>3пП</number><offset>0</offset><pk>5</pk><RADIUS>4300</RADIUS><SPEED>120</SPEED><stopSeq>2</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622068</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>40</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093468</idtf><km>644</km><latitude>6346333.0</latitude><longitude>634123.0</longitude><m>80</m><number>5пП</number><offset>0</offset><pk>5</pk><RADIUS>4300</RADIUS><SPEED>120</SPEED><stopSeq>3</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622077</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>52</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093469</idtf><km>644</km><m>20</m><number>7пП</number><offset>0</offset><pk>6</pk><SPEED>120</SPEED><stopSeq>4</stopSeq><suspensionType>КС-160</suspensionType><WireType>МФ-100</WireType><zigzag>30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622082</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>42</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093470</idtf><km>644</km><m>72</m><number>9п</number><offset>0</offset><pk>6</pk><SPEED>120</SPEED><stopSeq>5</stopSeq><suspensionType>КС-160</suspensionType><WireType>МФ-100</WireType><zigzag>15</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622087</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>55</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093471</idtf><km>644</km><m>14</m><number>11п</number><offset>0</offset><pk>7</pk><SPEED>120</SPEED><stopSeq>6</stopSeq><suspensionType>КС-160</suspensionType><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622084</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>60</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093472</idtf><km>644</km><m>69</m><number>13п</number><offset>0</offset><pk>7</pk><SPEED>120</SPEED><stopSeq>7</stopSeq><suspensionType>КС-160</suspensionType><WireType>МФ-100</WireType><zigzag>30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622067</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>64</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093473</idtf><km>644</km><m>29</m><number>15п</number><offset>0</offset><pk>8</pk><SPEED>120</SPEED><stopSeq>8</stopSeq><suspensionType>КС-160</suspensionType><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622071</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>61</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093474</idtf><km>644</km><m>93</m><number>17п</number><offset>0</offset><pk>8</pk><RADIUS>4260</RADIUS><SPEED>120</SPEED><stopSeq>9</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622083</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>56</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093475</idtf><km>644</km><m>54</m><number>19п</number><offset>0</offset><pk>9</pk><RADIUS>4260</RADIUS><SPEED>120</SPEED><stopSeq>10</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622075</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>48</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093476</idtf><km>644</km><m>10</m><number>21п</number><offset>0</offset><pk>10</pk><RADIUS>4260</RADIUS><SPEED>120</SPEED><stopSeq>11</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622086</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>41</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093477</idtf><km>644</km><m>58</m><number>23п</number><offset>0</offset><pk>10</pk><RADIUS>4260</RADIUS><SPEED>120</SPEED><stopSeq>12</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>15</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622074</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>43</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093478</idtf><km>644</km><m>99</m><number>25п</number><offset>0</offset><pk>10</pk><SPEED>120</SPEED><stopSeq>13</stopSeq><suspensionType>КС-160</suspensionType><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622078</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>48</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093479</idtf><km>645</km><m>42</m><number>27п</number><offset>0</offset><pk>1</pk><SPEED>120</SPEED><stopSeq>14</stopSeq><suspensionType>КС-160</suspensionType><WireType>МФ-100</WireType><zigzag>30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622073</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>35</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093480</idtf><km>645</km><m>90</m><number>29пС</number><offset>0</offset><pk>1</pk><RADIUS>1570</RADIUS><SPEED>120</SPEED><stopSeq>15</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-40</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622076</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>41</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093481</idtf><km>645</km><m>25</m><number>31п</number><offset>0</offset><pk>2</pk><RADIUS>1570</RADIUS><SPEED>120</SPEED><stopSeq>16</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>15</zigzag></Tower></FullTower><FullTower><Additionals><Additional><changeDate>18.04.2021 20:04:26</changeDate><number>2r</number><type>syper</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><number>4r</number><type>syper</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><number>5r</number><type>ko syper</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><latitude>333.0</latitude><longitude>123.0</longitude><number>6r</number><type>syper</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><number>7r</number><type>sper</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><number>8r</number><type>sypr</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><number>9r</number><type>sypREer</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><number>10r</number><type>syperRR</type></Additional><Additional><changeDate>18.04.2021 20:04:26</changeDate><latitude>333.0</latitude><longitude>123.0</longitude><number>1r</number><type>ultra syper</type></Additional></Additionals><Tower><assetNum>A26622088</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>47</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093482</idtf><km>645</km><m>66</m><number>33п</number><offset>0</offset><pk>2</pk><RADIUS>1570</RADIUS><SPEED>120</SPEED><stopSeq>17</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622085</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>58</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093483</idtf><km>645</km><m>13</m><number>35пС</number><offset>0</offset><pk>3</pk><RADIUS>1570</RADIUS><SPEED>120</SPEED><stopSeq>18</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-40</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622081</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>54</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093484</idtf><km>645</km><m>71</m><number>37п</number><offset>0</offset><pk>3</pk><RADIUS>2170</RADIUS><SPEED>120</SPEED><stopSeq>19</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622072</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>52</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093485</idtf><km>645</km><m>25</m><number>39п</number><offset>150</offset><pk>4</pk><RADIUS>2170</RADIUS><SPEED>120</SPEED><stopSeq>20</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622080</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>44</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093486</idtf><km>645</km><m>77</m><number>41п</number><offset>200</offset><pk>4</pk><RADIUS>2170</RADIUS><SPEED>120</SPEED><stopSeq>21</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-15</zigzag></Tower></FullTower><FullTower><Additionals/><Tower><assetNum>A26622079</assetNum><catenary>66</catenary><CountWire>2</CountWire><distance>49</distance><Grounded>1</Grounded><height>600</height><idtf>TF3093487</idtf><km>645</km><m>21</m><number>43п</number><offset>200</offset><pk>5</pk><RADIUS>2170</RADIUS><SPEED>120</SPEED><stopSeq>22</stopSeq><suspensionType>КС-160</suspensionType><TURN>R</TURN><WireType>МФ-100</WireType><zigzag>-30</zigzag></Tower></FullTower></Towers></SectionCertificate>"

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
//            when {
//                serializer.validate(SectionCertificate::class.java, file) -> {
//                    passportImport(file, liveData, error)
//                }
//                serializer.validate(FullSectionCertificate::class.java, file) -> {
            fullTowerImport(file, liveData, error)
//                }
//                else -> {
//                    throw RuntimeException("Wrong file type")
//                }
//            }
            println("-------------------- Import done well! -----------------------")
        }
        return error
    }

    private fun passportImport(file : File, liveData: MutableLiveData<WorkResult>,
                               error : WorkResult.Error) {
        try {
//            val sectionCertificate = serializer.read(SectionCertificate::class.java, file)
            val sectionCertificate = serializer.read(SectionCertificate::class.java, testPassport)

            if (sectionCertificate.passport == null){
                throw RuntimeException("Passport from file{$file} is empty!")
            }

            val passport_id = passportRepository.addPassport(Converter.fromXmlToPassport(sectionCertificate.passport!!))

            val towersSize = sectionCertificate.towers.size
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
            println(ex)
            error.addError(ex)
            liveData.postValue(error)
        }

    }

    private fun fullTowerImport(file : File, liveData: MutableLiveData<WorkResult>, error : WorkResult.Error) {
        try {

//            val fullTower = serializer.read(FullSectionCertificate::class.java, file)
            val fullTower = serializer.read(FullSectionCertificate::class.java, test2)

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

            val towersSize = fullTower.towers.size
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
            println(ex)
            println(ex.stackTrace)
            error.addError(ex)
            liveData.postValue(error)
        }
    }

}