package com.example.myapplication.exchange.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Additional", strict = false)
data class XMLAdditionalDto(
    @field:Element(required = false) var longitude: Double? = null,
    @field:Element(required = false) var latitude: Double? = null,
    @field:Element(required = false) var changeDate: String? = null,
    @field:Element(required = false) var type: String? = null,
    @field:Element(required = false) var number: String? = null
)