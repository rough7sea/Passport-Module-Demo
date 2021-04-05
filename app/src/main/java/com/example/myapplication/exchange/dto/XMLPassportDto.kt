package com.example.myapplication.exchange.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class XMLPassportDto(
    @JacksonXmlElementWrapper(localName = "CHANGEDATE")
    var changeDate: String? = "",
    var sectionName: String = "",
    var siteId: Long? = 0,
    var sectionId: String? = "",
    var echName: String? = "",
    var echkName: String? = "",
    var locationId: String? = "",
    var wayAmount: Int? = 0,
    var currentWay: String? = "",
    @JacksonXmlElementWrapper(localName = "currentWayID")
    var currentWayId: Long? = 0,
    var initialMeter: Int? = 0,
    @JacksonXmlElementWrapper(localName = "initialKM")
    var initialKm: Int? = 0,
    @JacksonXmlElementWrapper(localName = "initialPK")
    var initialPk: Int? = 0,
    @JacksonXmlElementWrapper(localName = "initialM")
    var initialM: Int? = 0,
    var plotLength: Int? = 0,
    var suspensionAmount: Int? = 0,
)