package com.example.myapplication.exchange.dto

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "TowerWithAdditionals")
@XmlAccessorType(XmlAccessType.FIELD)
data class TowerWithAdditionals(
    val Tower: XMLTowerDto? = null,
    @XmlElementWrapper(name = "Additionals")
    val Additional: List<XMLAdditionalDto> = mutableListOf()
)