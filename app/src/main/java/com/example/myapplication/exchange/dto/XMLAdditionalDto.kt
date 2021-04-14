package com.example.myapplication.exchange.dto

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "additional")
@XmlAccessorType(XmlAccessType.FIELD)
data class XMLAdditionalDto(
    var longitude: Double? = null,
    var latitude: Double? = null,
    var changeDate: String? = null,
    var type: String? = null,
    var number: String? = null
)