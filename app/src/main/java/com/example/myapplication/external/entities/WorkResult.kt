package com.example.myapplication.external.entities

sealed class WorkResult(
    val progress: Int? = 0,
    val errors: MutableList<Exception> = mutableListOf()
) {
    class Completed : WorkResult()
    class Loading : WorkResult()
    class Progress(progress: Int): WorkResult(progress = progress)
    class Canceled : WorkResult()
    class Error(): WorkResult(){
        fun addError(error: Exception){
            this.errors.add(error)
        }
        fun addErrors(errors: MutableList<Exception>){
            this.errors.addAll(errors)
        }
    }
}