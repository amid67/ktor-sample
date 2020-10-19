package com.raywenderlich.membership.logout.repository

import org.jetbrains.exposed.sql.ResultRow

interface LogoutRepository {
    suspend fun findUserByToken(token: String): ResultRow?
    suspend fun clearToken(userId:Int):Int
}