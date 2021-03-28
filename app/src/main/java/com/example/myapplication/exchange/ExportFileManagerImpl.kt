package com.example.myapplication.exchange

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

class ExportFileManagerImpl: ExportFileManager {

    private val xmlMapper = XmlMapper()

    override fun <B> export(bindingEntity: B, destinationPath: File) {
        try {
            destinationPath.writeBytes(xmlMapper.writeValueAsBytes(bindingEntity))
        } catch (ex: Exception){

        }
    }

    override fun <B> export(bindingEntity: List<B>, destinationPath: File) {
        try {
            bindingEntity.forEach { destinationPath.writeBytes(xmlMapper.writeValueAsBytes(it)) }
        } catch (ex: Exception){

        }
    }

}