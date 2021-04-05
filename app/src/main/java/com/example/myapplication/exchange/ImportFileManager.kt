package com.example.myapplication.exchange

import androidx.lifecycle.LiveData
import com.example.myapplication.external.entities.WorkResult
import java.io.File

interface ImportFileManager {
    fun import(file: File): LiveData<WorkResult>
    fun import(files: List<File>): LiveData<WorkResult>
}