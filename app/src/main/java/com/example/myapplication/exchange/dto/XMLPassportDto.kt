package com.example.myapplication.exchange.dto

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement


@XmlRootElement(name = "Header")
@XmlAccessorType(XmlAccessType.FIELD)
data class XMLPassportDto(
    var siteId: Long? = null,
    var sectionName: String = "",
    var sectionId: String? = null,
    var echName: String? = null,
    var echkName: String? = null,
    var locationId: String? = null,
    var wayAmount: Int? = null,
    var currentWay: String? = null,
    var currentWayID: Long? = null,
    var CHANGEDATE: String? = null,
    var initialMeter: Int? = null,
    var initialKM: Int? = null,
    var initialPK: Int? = null,
    var initialM: Int? = null,
    var plotLength: Int? = null,
    var suspensionAmount: Int? = null,
)