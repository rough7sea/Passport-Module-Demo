package com.example.myapplication.exchange.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Tower", strict = false)
data class XMLTowerDto(
    @field:Element(name = "idtf", required = false) var idtf: String? = null,
    @field:Element(required = false) var assetNum: String? = null,
    @field:Element(required = false) var stopSeq: Int? = null,
    @field:Element(required = false) var km: Int? = null,
    @field:Element(required = false) var pk: Int? = null,
    @field:Element(required = false) var m: Int? = null,
    @field:Element(name = "TF_TYPE", required = false) var tfType: String? = null,
    @field:Element(name = "TURN", required = false) var turn: String? = null,
    @field:Element(name = "RADIUS", required = false) var radius: String? = null,
    @field:Element(required = false) var number: String? = null,
    @field:Element(required = false) var distance: Int? = null,
    @field:Element(required = false) var zigzag: Int? = null,
    @field:Element(required = false) var height: Int? = null,
    @field:Element(required = false) var offset: Int? = null,
    @field:Element(name = "Grounded", required = false) var grounded: Int? = null,
    @field:Element(name = "SPEED", required = false) var speed: Int? = null,
    @field:Element(required = false) var suspensionType: String? = null,
    @field:Element(required = false) var catenary: Int? = null,
    @field:Element(name = "WireType", required = false) var wireType: String? = null,
    @field:Element(name = "CountWire", required = false) var countWire: String? = null,
    @field:Element(required = false) var longitude: Double? = null,
    @field:Element(required = false) var latitude: Double? = null,
    @field:Element(name = "Gabarit", required = false) var gabarit: String? = null,
)