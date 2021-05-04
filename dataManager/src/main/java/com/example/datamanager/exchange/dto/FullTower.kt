package com.example.datamanager.exchange.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

/**
 * Dto entity for deserialization internal data into xml.
 */
@Root(name = "FullTower", strict = false)
data class FullTower(
    @field:Element(name = "Tower", required = false)
    var tower: XMLTowerDto? = null,
    @field:Path("Additionals/[1]")
    @field:ElementList(inline = true, required = false)
    var additionals: List<XMLAdditionalDto> = mutableListOf()
)