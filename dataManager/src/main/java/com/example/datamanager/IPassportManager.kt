package com.example.datamanager

import com.example.datamanager.database.repository.RepositoryProvider
import com.example.datamanager.exchange.ExportFileManager
import com.example.datamanager.exchange.ImportFileManager
import com.example.datamanager.external.handler.ObjectBindingHandler
import com.example.datamanager.search.SearchLocationObjectManager

interface IPassportManager<T> :
        ImportFileManager,
        ExportFileManager,
        SearchLocationObjectManager<T>,
        ObjectBindingHandler,
        RepositoryProvider