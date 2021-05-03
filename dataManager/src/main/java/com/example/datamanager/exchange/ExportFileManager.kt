package com.example.datamanager.exchange

import androidx.lifecycle.LiveData
import com.example.datamanager.external.entities.WorkResult
import java.io.File

interface ExportFileManager{

    fun <B : Any> export(bindingEntity: B, destinationPath: File): LiveData<WorkResult>
//    fun <B> export(bindingEntity: List<B>, destinationPath: File): LiveData<WorkResult>

//    fun <B, T> export(bindingEntity: B)
//    fun <B, T> export(bindingEntity: List<B>)
}