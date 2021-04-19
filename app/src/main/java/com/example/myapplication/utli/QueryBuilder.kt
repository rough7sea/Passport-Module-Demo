package com.example.myapplication.utli

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.myapplication.database.DatabaseConst
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.entity.Coordinate
import com.example.myapplication.database.entity.Passport
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.exchange.dto.XMLPassportDto
import java.util.*
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

object QueryBuilder {

    // radius in meters
    fun buildCoordInRadiusQuery(longitude: Double, latitude: Double, radius: Int) =
            SimpleSQLiteQuery("""
                SELECT * FROM ${DatabaseConst.COORDINATE_TABLE_NAME} WHERE 
                 6371 * 2 * atan2(sqrt(
                 power(SIN((RADIANS(latitude) - RADIANS($latitude))) / 2 , 2) + 
                 COS(RADIANS($latitude)) * 
                 COS(RADIANS(latitude)) * 
                 power(SIN((RADIANS(longitude) - RADIANS($longitude))) / 2 , 2) 
                ),
                 sqrt(1 - 
                 power(SIN((RADIANS(latitude) - RADIANS($latitude))) / 2 , 2) + 
                 COS(RADIANS($latitude)) * 
                 COS(RADIANS(latitude)) * 
                 power(SIN((RADIANS(longitude) - RADIANS($longitude))) / 2 , 2) 
                )) * 1000 
                 <= $radius; 
            """.trimIndent())


    fun <T> buildSelectQuery(
            clazz: Class<T>, values: List<Pair<String, String>> = arrayListOf()
    ): SupportSQLiteQuery {
        val query =
                StringBuilder("SELECT * FROM ${clazz.simpleName.toLowerCase(Locale.getDefault())}")

        if (!values.isNullOrEmpty() && values.size > clazz.declaredFields.size) {
            for (i in values.indices) {
                if (i == 0)
                    query.append(" WHERE ${values[i].first} = ${values[i].second}")
                else
                    query.append(" AND ${values[i].first} = ${values[i].second}")
            }
        }
        return SimpleSQLiteQuery(query.toString())
    }

    fun <T> buildCountQuery(clazz: Class<T>) =
            SimpleSQLiteQuery("SELECT COUNT(*) FROM ${clazz.simpleName.toLowerCase(Locale.getDefault())}")

    fun buildLastTimestampQuery() =
            SimpleSQLiteQuery("SELECT MAX(changeDate) FROM " +
                    "(SELECT changeDate FROM ${DatabaseConst.PASSPORT_TABLE_NAME} UNION ALL " +
                    "SELECT changeDate FROM ${DatabaseConst.TOWER_TABLE_NAME} UNION ALL " +
                    "SELECT changeDate FROM ${DatabaseConst.ADDITIONAL_TABLE_NAME} UNION ALL " +
                    "SELECT changeDate FROM ${DatabaseConst.COORDINATE_TABLE_NAME})")

    fun <E> buildDeleteWithParametersQuery(bindObject: E): SimpleSQLiteQuery {
        val query = StringBuilder("DELETE FROM ")
        buildParametersQuery(bindObject, query)
        return SimpleSQLiteQuery(query.toString())
    }

    fun <E> buildSearchByAllFieldsQuery(bindObject: E): SimpleSQLiteQuery {
        val query = StringBuilder("SELECT * FROM ")
        buildParametersQuery(bindObject, query)
        return SimpleSQLiteQuery(query.toString())
    }

    private fun <E> buildParametersQuery(bindObject: E, query: StringBuilder){
        when (bindObject) {
            is Passport -> {
                query.append("${DatabaseConst.PASSPORT_TABLE_NAME} WHERE")
                query.append(fullPassportParameters(bindObject))
            }
            is XMLPassportDto -> {
                query.append("${DatabaseConst.PASSPORT_TABLE_NAME} WHERE")
                query.append(fullPassportParameters(bindObject))
            }
            is Tower -> {
                query.append("${DatabaseConst.TOWER_TABLE_NAME} WHERE")
                query.append(fullTowerParameters(bindObject))
            }
            is Additional -> {
                query.append("${DatabaseConst.ADDITIONAL_TABLE_NAME} WHERE ")
                query.append(fullAdditionalParameters(bindObject))
            }
            is Coordinate -> {
                query.append("${DatabaseConst.COORDINATE_TABLE_NAME} WHERE ")
                query.append(fullCoordinateParameters(bindObject))
            }
        }
        if (query.contains("AND")){
            query.delete(query.lastIndex - 2, query.lastIndex + 1)
        }
        query.append(';')
    }

    private fun fullCoordinateParameters(coordinate: Coordinate) : StringBuilder{
        val query = StringBuilder()
        with(coordinate){
            query.append(" latitude = $latitude AND")
            query.append(" longitude = $longitude AND")
        }
        return query
    }
    private fun fullAdditionalParameters(additional: Additional) : StringBuilder{
        val query = StringBuilder()
        with(additional){
            query.append(" tower_id = $tower_id AND")
            query.append(" number = '$number' AND")
        }
        return query
    }
    private fun fullPassportParameters(passport: Passport) : StringBuilder{
        val query = StringBuilder()
        with(passport){
            if (siteId != null) query.append(" siteId = $siteId AND ")
            query.append(" sectionName = '$sectionName' AND")
            if (sectionId != null) query.append(" sectionId = '$sectionId' AND")
            if (echName != null) query.append(" echName = '$echName' AND")
            if (echkName != null) query.append(" echkName = '$echkName' AND")
            if (locationId != null) query.append(" locationId = '$locationId' AND")
            if (wayAmount != null) query.append(" wayAmount = $wayAmount AND")
            if (currentWay != null) query.append(" currentWay = '$currentWay' AND")
            if (currentWayId != null) query.append(" currentWayId = $currentWayId AND")
            if (initialMeter != null) query.append(" initialMeter = $initialMeter AND")
            if (initialKm != null) query.append(" initialKm = $initialKm AND")
            if (initialPk != null) query.append(" initialPk = $initialPk AND")
            if (initialM != null) query.append(" initialM = $initialM AND")
            if (plotLength != null) query.append(" plotLength = $plotLength AND")
            if (suspensionAmount != null) query.append(" suspensionAmount = $suspensionAmount AND")
        }
        return query
    }
    private fun fullPassportParameters(passport: XMLPassportDto) : StringBuilder{
        val query = StringBuilder()
        with(passport){
            if (siteId != null) query.append(" siteId = $siteId AND ")
            query.append(" sectionName = '$sectionName' AND")
            if (sectionId != null) query.append(" sectionId = '$sectionId' AND")
            if (echName != null) query.append(" echName = '$echName' AND")
            if (echkName != null) query.append(" echkName = '$echkName' AND")
            if (locationId != null) query.append(" locationId = '$locationId' AND")
            if (wayAmount != null) query.append(" wayAmount = $wayAmount AND")
            if (currentWay != null) query.append(" currentWay = '$currentWay' AND")
            if (currentWayID != null) query.append(" currentWayId = $currentWayID AND")
            if (initialMeter != null) query.append(" initialMeter = $initialMeter AND")
            if (initialKM != null) query.append(" initialKm = $initialKM AND")
            if (initialPK != null) query.append(" initialPk = $initialPK AND")
            if (initialM != null) query.append(" initialM = $initialM AND")
            if (plotLength != null) query.append(" plotLength = $plotLength AND")
            if (suspensionAmount != null) query.append(" suspensionAmount = $suspensionAmount AND")
        }
        return query
    }

    private fun fullTowerParameters(tower: Tower) : StringBuilder{
        val query = StringBuilder()
        with(tower){
            query.append(" passport_id = $passport_id AND")
//            if (idtf.isNotEmpty())
            query.append(" idtf = '$idtf' AND")
//            if (assetNum.isNotEmpty())
            query.append(" assetNum = '$assetNum' AND")
            query.append(" stopSeq = $stopSeq AND")
            query.append(" km = $km AND")
            query.append(" pk = $pk AND")
            query.append(" m = $m AND")
//            if (tfType.isNotEmpty()) query.append(" tfType = '$tfType' AND")
//            if (turn.isNotEmpty()) query.append(" turn = '$turn' AND")
//            if (radius.isNotEmpty()) query.append(" radius = '$radius' AND")
            query.append(" number = $number AND")
            query.append(" distance = $distance AND")
            query.append(" zigzag = $zigzag AND")
            query.append(" height = $height AND")
            query.append(" offset = $offset AND")
            query.append(" grounded = $grounded AND")
            query.append(" speed = $speed AND")
//            if (suspensionType.isNotEmpty()) query.append(" suspensionType = '$suspensionType' AND")
//            query.append(" catenary = $catenary AND")
//            if (wireType.isNotEmpty()) query.append(" wireType = '$wireType' AND")
//            if (countWire.isNotEmpty()) query.append(" countWire = '$countWire' AND")
        }
        return query
    }

    private fun fullAlter(passport: Passport) : String{
        val query = StringBuilder()
        val excludeFields = listOf("passport_id", "changeDate")
        passport::class.memberProperties.forEach {
            if (!excludeFields.contains(it.name) && it.visibility == KVisibility.PUBLIC){
                val value = it.getter.call(passport)
                if (value != null){
                    query.append(" ${it.name} = ")
                    if (value is String && value.isNotEmpty()){
                        query.append("'$value'")
                    } else {
                        query.append("$value")
                    }
                    query.append(" AND")
                }
            }
        }
        if (query.contains("AND")){
            query.delete(query.lastIndex - 2, query.lastIndex + 1)
        }
        query.append(';')
        return query.toString()
    }
}