package com.raywenderlich.core.model

sealed class Respond<out R> {
    data class Success<out R>(val data: R) : Respond<R>()
    data class Error(val code: Int,val message:String) : Respond<Nothing>()
}