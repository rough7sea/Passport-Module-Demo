package com.example.datamanager.exchange.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root


/**
 * Dto entity for deserialization internal data into xml.
 */
@Root(name = "SectionCertificate", strict = false)
data class SectionCertificate(
    @field:Element(name = "Header", required = true)
    var passport: XMLPassportDto? = null,
    @field:Path("Towers/[1]")
    @field:ElementList(inline = true, required = false)
    var towers: List<XMLTowerDto> = mutableListOf(),
)