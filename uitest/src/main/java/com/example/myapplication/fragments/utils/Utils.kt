package com.example.myapplication.fragments.utils

import android.text.TextUtils

object Utils {

    fun validate(vararg args: String): Boolean {
        for (a in args){
            if (TextUtils.isEmpty(a)){
                return false
            }
        }
        return true
    }
}