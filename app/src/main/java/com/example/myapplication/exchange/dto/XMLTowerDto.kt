package com.example.myapplication.exchange.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
data class XMLTowerDto(

    val idtf: String? = "",
    val assetNum: String? = "",
    val stopSeq: Int? = 0,
    val km: Int? = 0,
    val pk: Int? = 0,
    val m: Int? = 0,
    @JacksonXmlElementWrapper(localName = "TF_TYPE")
    val tfType: String? = "",
    @JacksonXmlElementWrapper(localName = "TURN")
    val turn: String? = "",
    @JacksonXmlElementWrapper(localName = "RADIUS")
    val radius: String? = "",
    val number: String? = "",
    val distance: Int? = 0,
    val zigzag: Int? = 0,
    val height: Int? = 0,
    val offset: Int? = 0,
    @JacksonXmlElementWrapper(localName = "Grounded")
    val grounded: Int? = 0,
    @JacksonXmlElementWrapper(localName = "SPEED")
    val speed: Int? = 0,
    val suspensionType: String? = "",
    val catenary: Int? = 0,
    @JacksonXmlElementWrapper(localName = "WireType")
    val wireType: String? = "",
    @JacksonXmlElementWrapper(localName = "CountWire")
    val countWire: String? = "",
    val longitude: Int? = 0,
    val latitude: Int? = 0,
    @JacksonXmlElementWrapper(localName = "Gabarit")
    val gabarit: String? = "",
)