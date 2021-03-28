package com.example.myapplication.exchange

import java.io.File

interface ExportFileManager{
    fun <B> export(bindingEntity: B, destinationPath: File)
    fun <B> export(bindingEntity: List<B>, destinationPath: File)

//    fun <B, T> export(bindingEntity: B)
//    fun <B, T> export(bindingEntity: List<B>)
}