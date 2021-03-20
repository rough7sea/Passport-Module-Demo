package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.dao.AdditionalDAO
import com.example.myapplication.entity.Additional

class AdditionalRepository(private val additionalDAO: AdditionalDAO) {

    val allAdditionalData: LiveData<List<Additional>> = additionalDAO.getAll()

    suspend fun addAdditional(additional: Additional){
        additionalDAO.insert(additional)
    }

}