package com.example.myapplication.exchange.dto

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Tower")
@XmlAccessorType(XmlAccessType.FIELD)
data class XMLTowerDto(
    var idtf: String? = null,
    var assetNum: String? = null,
    var stopSeq: Int? = null,
    var km: Int? = null,
    var pk: Int? = null,
    var m: Int? = null,
    var TF_TYPE: String? = null,
    var TURN: String? = null,
    var RADIUS: String? = null,
    var number: String? = null,
    var distance: Int? = null,
    var zigzag: Int? = null,
    var height: Int? = null,
    var offset: Int? = null,
    var Grounded: Int? = null,
    var SPEED: Int? = null,
    var suspensionType: String? = null,
    var catenary: Int? = null,
    var WireType: String? = null,
    var CountWire: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null,
    var Gabarit: String? = null,
)