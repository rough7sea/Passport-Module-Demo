package com.example.datamanager.exchange

import androidx.lifecycle.LiveData
import com.example.datamanager.external.entities.WorkResult
import java.io.File

/**
 * Interface fot export data service. Use to upload data from internal storage in xml.
 */
interface ExportFileManager{

    /**
     * Upload object tree [Passport] -> [Tower] -> [Additional].
     * Use [FullSectionCertificate] as xml-schema.
     *
     * @param bindingEntity one of the tree object.
     * @param destinationPath output file. Application must be with *WRITE_EXTERNAL_STORAGE* access
     * to write data into file.
     * @return [LiveData] with [WorkResult] value.
     */
    fun <B : Any> export(bindingEntity: B, destinationPath: File): LiveData<WorkResult>

    fun getExportResult(): LiveData<WorkResult>
}