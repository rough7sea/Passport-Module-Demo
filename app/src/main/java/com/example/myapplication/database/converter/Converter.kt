package com.example.myapplication.database.converter

import androidx.room.TypeConverter
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.entity.Coordinate
import com.example.myapplication.database.entity.Passport
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.exchange.dto.XMLAdditionalDto
import com.example.myapplication.exchange.dto.XMLPassportDto
import com.example.myapplication.exchange.dto.XMLTowerDto
import java.text.SimpleDateFormat
import java.util.*

class Converter {

    @TypeConverter
    fun getDateFromTimestamp(value: Long?) = value?.let { Date(value) }

    @TypeConverter
    fun getTimestampFromDate(value: Date?) = value?.time

    companion object {
        private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:MM:SS", Locale.getDefault())

        fun fromStringToDate(strDate: String): Date {
            return simpleDateFormat.parse(strDate) ?: throw RuntimeException("Wrong Date format!")
        }

        fun fromXmlToPassport(dto: XMLPassportDto): Passport {
            with(dto) {
                return Passport(0, changeDate?.let { fromStringToDate(it) } ?: Date(), sectionName,
                    siteId, sectionId, echName, echkName, locationId, wayAmount,
                    currentWay, currentWayID, initialMeter, initialKM, initialPK,
                    initialM, plotLength, suspensionAmount)
            }
        }

        fun fromPassportToXml(passport: Passport): XMLPassportDto {
            with(passport) {
                return XMLPassportDto(siteId,  sectionName,
                     sectionId, echName, echkName, locationId, wayAmount,
                    currentWay, currentWayId, simpleDateFormat.format(passport.changeDate), initialMeter,
                    initialKm, initialPk, initialM, plotLength, suspensionAmount)
            }
        }

        fun fromXmlToTower(dto: XMLTowerDto, coord_id: Long?): Tower {
            with(dto) {
                return Tower(
                    0, 0, coord_id,
                    Date(), idtf ?: "", assetNum ?: "",
                    stopSeq, km, pk, m,
                    tfType , turn, radius, number ?: "",
                    distance, zigzag, height, offset,
                    grounded, speed, suspensionType,
                    catenary, wireType, countWire, gabarit
                )
            }
        }

        fun fromTowerToXml(tower: Tower, coordinate: Coordinate?): XMLTowerDto {
            with(tower) {
                return XMLTowerDto(
                    idtf, assetNum,
                    stopSeq, km, pk, m,
                    tfType , turn, radius, number,
                    distance, zigzag, height, offset,
                    grounded, speed, suspensionType,
                    catenary, wireType, countWire,
                    coordinate?.longitude, coordinate?.latitude, gabarit
                )
            }
        }

        fun fromXmlToAdditional(dto: XMLAdditionalDto, tower_id: Long, coord_id: Long?): Additional{
            with(dto){
                return Additional(0, tower_id, coord_id,
                    changeDate?.let { fromStringToDate(it) } ?: Date(),
                    type, number ?: "")
            }
        }

        fun fromAdditionalToXml(additional: Additional, coordinate: Coordinate?): XMLAdditionalDto{

            with(additional){
                return XMLAdditionalDto(coordinate?.longitude, coordinate?.latitude,
                    simpleDateFormat.format(additional.changeDate), type, number
                )
            }
        }

    }
}