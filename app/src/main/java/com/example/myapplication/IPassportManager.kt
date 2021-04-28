package com.example.myapplication

import com.example.myapplication.exchange.ExportFileManager
import com.example.myapplication.exchange.ImportFileManager
import com.example.myapplication.external.handler.ObjectBindingHandler
import com.example.myapplication.search.SearchLocationObjectManager

interface IPassportManager<T> :
        ImportFileManager,
        ExportFileManager,
        SearchLocationObjectManager<T>,
        ObjectBindingHandler