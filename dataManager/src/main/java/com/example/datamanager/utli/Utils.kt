package com.example.datamanager.utli

object Utils{
    private val nonLetterRegex = Regex("[A-Za-zА-Яа-я]")

    fun clearNumber(number: String) = nonLetterRegex.replace(number, "")

}