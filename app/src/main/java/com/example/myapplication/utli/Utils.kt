package com.example.myapplication.utli

import java.text.SimpleDateFormat
import java.util.*

object Utils{
    private val nonLetterRegex = Regex("[A-Za-zА-Яа-я]")

    fun clearNumber(number: String) = nonLetterRegex.replace(number, "")

}