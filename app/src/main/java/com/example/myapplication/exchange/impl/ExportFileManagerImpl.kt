package com.example.myapplication.exchange.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.exchange.ExportFileManager
import com.example.myapplication.external.entities.WorkResult
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ExportFileManagerImpl: ExportFileManager {

    private val xmlMapper = XmlMapper()

    override fun <B> export(bindingEntity: B, destinationPath: File) : LiveData<WorkResult> {
        val result = MutableLiveData<WorkResult>()
        CoroutineScope(Dispatchers.IO).launch {
            val error = WorkResult.Error()

            result.postValue(WorkResult.Progress(0))
            try {
                destinationPath.writeBytes(xmlMapper.writeValueAsBytes(bindingEntity))
                result.postValue(WorkResult.Progress(100))
            } catch (ex: Exception){
                error.addError(ex)
            }

            if (error.errors.isNotEmpty()){
                result.postValue(error)
            } else {
                result.postValue(WorkResult.Completed())
            }
        }
        return result
    }

    override fun <B> export(bindingEntity: List<B>, destinationPath: File) : LiveData<WorkResult>{
        val result = MutableLiveData<WorkResult>()
        CoroutineScope(Dispatchers.IO).launch {
            val error = WorkResult.Error()

            val size = bindingEntity.size
            val step = 100 / size
            var progress = 0

            result.postValue(WorkResult.Progress(0))
            bindingEntity.forEach {
                try {
                    destinationPath.writeBytes(xmlMapper.writeValueAsBytes(it))
                    result.postValue(WorkResult.Progress(progress + step))
                    progress += step
                } catch (ex: Exception){
                    error.addError(ex)
                }
            }

            if (error.errors.isNotEmpty()){
                result.postValue(error)
            } else {
                result.postValue(WorkResult.Completed())
            }
        }
        return result
    }
}