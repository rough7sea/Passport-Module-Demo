package com.example.datamanager.exchange.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Header", strict = false)
data class XMLPassportDto(
        @field:Element(required = false) var siteId: Long? = null,
        @field:Element(required = false) var sectionName: String? = null,
        @field:Element(required = false) var sectionId: String? = null,
        @field:Element(required = false) var echName: String? = null,
        @field:Element(required = false) var echkName: String? = null,
        @field:Element(required = false) var locationId: String? = null,
        @field:Element(required = false) var wayAmount: Int? = null,
        @field:Element(required = false) var currentWay: String? = null,
        @field:Element(required = false) var currentWayID: Long? = null,
        @field:Element(name = "CHANGEDATE", required = false) var changeDate: String? = null,
        @field:Element(required = false) var initialMeter: Int? = null,
        @field:Element(required = false) var initialKM: Int? = null,
        @field:Element(required = false) var initialPK: Int? = null,
        @field:Element(required = false) var initialM: Int? = null,
        @field:Element(required = false) var plotLength: Int? = null,
        @field:Element(required = false) var suspensionAmount: Int? = null,
)