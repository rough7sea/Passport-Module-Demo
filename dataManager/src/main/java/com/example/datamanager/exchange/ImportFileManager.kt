package com.example.datamanager.exchange

import androidx.lifecycle.LiveData
import com.example.datamanager.exchange.dto.FullSectionCertificate
import com.example.datamanager.exchange.dto.SectionCertificate
import com.example.datamanager.external.entities.WorkResult
import java.io.File

/**
 * Interface fot import data service. Use to load data from internal storage in xml.
 */
interface ImportFileManager {
    /**
     * Import data into the system.
     * @param [file] XML file from storage. Expected [FullSectionCertificate] or [SectionCertificate]
     * data signature.
     * @return [LiveData] with [WorkResult] value.
     */
    fun import(file: File): LiveData<WorkResult>
    /**
     * Import data into the system.
     * @param [files] XML files from storage. Expected [FullSectionCertificate] or [SectionCertificate]
     * data signature.
     * @return [LiveData] with [WorkResult] value.
     */
    fun import(files: List<File>): LiveData<WorkResult>
}