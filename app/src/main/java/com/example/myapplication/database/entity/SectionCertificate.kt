package com.example.myapplication.database.entity

import com.example.myapplication.exchange.dto.XMLPassportDto
import com.example.myapplication.exchange.dto.XMLTowerDto
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

/**
 * For xml deserialization.
 */
data class SectionCertificate(
    @JacksonXmlElementWrapper(localName = "Header", namespace = "Header")
        val header: XMLPassportDto? = null,
    @JacksonXmlElementWrapper(localName = "Towers", namespace = "Towers")
        val towers: List<XMLTowerDto> = emptyList(),
)