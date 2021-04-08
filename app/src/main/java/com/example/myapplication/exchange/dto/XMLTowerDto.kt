package com.example.myapplication.exchange.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

@JsonIgnoreProperties(ignoreUnknown = true)
data class XMLTowerDto(

    var idtf: String? = "",
    var assetNum: String? = "",
    var stopSeq: Int? = 0,
    var km: Int? = 0,
    var pk: Int? = 0,
    var m: Int? = 0,
    @JacksonXmlElementWrapper(localName = "TF_TYPE")
    var tfType: String? = "",
    @JacksonXmlElementWrapper(localName = "TURN")
    var turn: String? = "",
    @JacksonXmlElementWrapper(localName = "RADIUS")
    var radius: String? = "",
    var number: String? = "",
    var distance: Int? = 0,
    var zigzag: Int? = 0,
    var height: Int? = 0,
    var offset: Int? = 0,
    @JacksonXmlElementWrapper(localName = "Grounded")
    var grounded: Int? = 0,
    @JacksonXmlElementWrapper(localName = "SPEED")
    var speed: Int? = 0,
    var suspensionType: String? = "",
    var catenary: Int? = 0,
    @JacksonXmlElementWrapper(localName = "WireType")
    var wireType: String? = "",
    @JacksonXmlElementWrapper(localName = "CountWire")
    var countWire: String? = "",
    var longitude: Double? = .0,
    var latitude: Double? = .0,
    @JacksonXmlElementWrapper(localName = "Gabarit")
    var gabarit: String? = "",
)