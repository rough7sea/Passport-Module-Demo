package com.example.datamanager.database.converter

import androidx.room.TypeConverter
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.exchange.dto.XMLAdditionalDto
import com.example.datamanager.exchange.dto.XMLPassportDto
import com.example.datamanager.exchange.dto.XMLTowerDto
import java.text.SimpleDateFormat
import java.util.*

/**
 * Util class.
 * Applied as [TypeConverter] in Room Database class.
 */
class Converter {

    /**
     * Converter method which used to convert [Long] timestamp value to [Date].
     */
    @TypeConverter
    fun getDateFromTimestamp(value: Long?) = value?.let { Date(value) }

    /**
     * Converter method which used to convert [Date] to [Long] timestamp value.
     */
    @TypeConverter
    fun getTimestampFromDate(value: Date?) = value?.time

    companion object {
        /**
         * Unique date format for this module.
         */
        private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:MM:SS", Locale.getDefault())

        /**
         * Convert to date from string.
         * @param strDate must match pattern <b>dd.MM.yyyy HH:MM:SS</b>
         * @throws RuntimeException if date format not match pattern.
         */
        fun fromStringToDate(strDate: String): Date {
            return simpleDateFormat.parse(strDate) ?:
            throw RuntimeException("Wrong Date format [$strDate]. Right format is [${simpleDateFormat.toPattern()}]!")
        }

        /**
         * Convert XML-dto to database object.
         * @param dto instant of [XMLPassportDto]
         * @return [Passport] object.
         */
        fun fromXmlToPassport(dto: XMLPassportDto): Passport {
            with(dto) {
                return Passport(0, changeDate?.let { fromStringToDate(it) } ?: Date(),
                    sectionName ?: "", siteId, sectionId, echName, echkName,
                    locationId, wayAmount, currentWay, currentWayID, initialMeter, initialKM,
                    initialPK, initialM, plotLength, suspensionAmount)
            }
        }

        /**
         * Convert database object to XML-dto.
         * @param passport instant of [Passport].
         * @return [XMLPassportDto] object.
         */
        fun fromPassportToXml(passport: Passport): XMLPassportDto {
            with(passport) {
                return XMLPassportDto(siteId,  sectionName,
                    sectionId, echName, echkName, locationId, wayAmount,
                    currentWay, currentWayId, simpleDateFormat.format(passport.changeDate), initialMeter,
                    initialKm, initialPk, initialM, plotLength, suspensionAmount)
            }
        }

        /**
         * Convert XML-dto to database object.
         * @param dto instant of [XMLTowerDto].
         * @param coord_id coordinate id. *Null* if no coordinate for this object.
         * @return [Tower] object.
         */
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

        /**
         * Convert database object to XML-dto.
         * @param tower instant of [Tower].
         * @param coordinate instant of [Coordinate]. Can be *null* if there are no coordinate for this object.
         * @return [XMLTowerDto] object.
         */
        fun fromTowerToXml(tower: Tower, coordinate: Coordinate?): XMLTowerDto {
            with(tower) {
                return XMLTowerDto(
                    idtf, assetNum,
                    stopSeq, km, pk, m,
                    tfType , turn, radius, number,
                    distance, zigzag, height, offset,
                    grounded, speed, suspensionType,
                    catenary, wireType, countWire,
                    coordinate?.latitude, coordinate?.longitude, gabarit
                )
            }
        }

        /**
         * Convert XML-dto to database object.
         * @param dto instant of [XMLTowerDto].
         * @param tower_id tower id to make binding between tower object.
         * @param coord_id coordinate id. *Null* if no coordinate for this object.
         * @return [Additional] object.
         */
        fun fromXmlToAdditional(dto: XMLAdditionalDto, tower_id: Long, coord_id: Long?): Additional{
            with(dto){
                return Additional(0, tower_id, coord_id,
                    changeDate?.let { fromStringToDate(it) } ?: Date(),
                    type, number ?: "")
            }
        }

        /**
         * Convert database object to XML-dto.
         * @param additional instant of [Additional].
         * @param coordinate instant of [Coordinate]. Can be *null* if there are no coordinate for this object.
         * @return [XMLAdditionalDto] object.
         */
        fun fromAdditionalToXml(additional: Additional, coordinate: Coordinate?): XMLAdditionalDto{
            with(additional){
                return XMLAdditionalDto(coordinate?.latitude, coordinate?.longitude,
                    simpleDateFormat.format(additional.changeDate), type, number
                )
            }
        }
    }
}