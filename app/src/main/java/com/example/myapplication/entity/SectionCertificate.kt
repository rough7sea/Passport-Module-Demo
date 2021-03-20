package com.example.myapplication.entity


/**
 * For xml deserialization.
 */
data class SectionCertificate(
//    @JacksonXmlElementWrapper(localName = "Header")
        val header: Passport? = null,
//    @JacksonXmlElementWrapper(localName = "Towers")
        val towers: List<Tower>? = null,
)