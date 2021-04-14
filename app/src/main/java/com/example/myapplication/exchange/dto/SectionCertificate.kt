package com.example.myapplication.exchange.dto

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement


/**
 * For xml deserialization.
 */
@XmlRootElement(name = "SectionCertificate")
@XmlAccessorType(XmlAccessType.FIELD)
data class SectionCertificate(
    val Header: XMLPassportDto? = null,
    @XmlElementWrapper(name = "Towers")
    val Tower: List<XMLTowerDto> = mutableListOf(),
)