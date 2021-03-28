package com.example.myapplication.database.converter

import androidx.room.TypeConverter
import com.example.myapplication.database.entity.Coordinate
import com.example.myapplication.database.entity.Passport
import com.example.myapplication.database.entity.Tower
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

        fun fromXMLToPassport(dto: XMLPassportDto): Passport {
            with(dto) {
                return Passport(0, fromStringToDate(changeDate), sectionName,
                        siteId, sectionId, echName, echkName, locationId, wayAmount,
                        currentWay, currentWayId, initialMeter, initialKm, initialPk,
                        initialM, plotLength, suspensionAmount)
            }
        }

        fun fromXMLTowerDtoToTower(dto: XMLTowerDto, coord: Coordinate): Tower {
            with(dto) {
                return Tower(
                        0, coord.coord_id, 0,
                        Date(), idtf ?: "", assetNum ?: "",
                        stopSeq?: 0, km?: 0, pk?: 0, m?: 0,
                        tfType ?: "", turn?: "", radius?: "", number?: "",
                        distance?: 0, zigzag?: 0, height?: 0, offset?: 0,
                        grounded?: 0, speed?: 0, suspensionType?: "",
                        catenary?: 0, wireType?: "", countWire?: "", gabarit?: ""
                )
            }
        }
    }
}