package com.example.myapplication.exchange.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "SectionCertificate", strict = false)
data class FullSectionCertificate(
    @field:Element(name = "Header", required = true)
    var passport: XMLPassportDto? = null,
    @field:Path("Towers/[1]")
    @field:ElementList(inline = true, required = false)
    var towers: List<FullTower> = mutableListOf(),
)