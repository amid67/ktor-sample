package com.raywenderlich.membership.signup.v1.repository

import com.raywenderlich.core.model.Respond
import com.raywenderlich.membership.signup.v1.entity.User

interface SignupRepository {
    suspend fun findUser(userId: Int): Respond<User>
    suspend fun addUser(username: String, passwordHash: String,jwtToken: String): Respond<User>
    suspend fun findUserByUsername(username: String): Respond<User>
}