package com.raywenderlich.membership.login.v1.repository

import org.jetbrains.exposed.sql.ResultRow

interface LoginRepository {
    suspend fun findUserByUsername(username: String): ResultRow?
    suspend fun updateToken(userId:Int,token:String):Int
}