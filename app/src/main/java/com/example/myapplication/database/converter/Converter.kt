package com.example.myapplication.database.converter

import androidx.room.TypeConverter
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
                return Passport(0, changeDate?.let { fromStringToDate(it) } ?: Date(), sectionName,
                        siteId, sectionId, echName, echkName, locationId, wayAmount,
                        currentWay, currentWayId, initialMeter, initialKm, initialPk,
                        initialM, plotLength, suspensionAmount)
            }
        }

        fun fromXMLTowerDtoToTower(dto: XMLTowerDto, coord_id: Long?): Tower {
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
    }
}