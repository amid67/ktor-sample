package com.raywenderlich.membership.login.v1.repository

import com.raywenderlich.extension.asyncDbQuery
import com.raywenderlich.membership.signup.v1.repository.UserEntity
import io.ktor.util.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

@KtorExperimentalAPI
class LoginRepositoryImpl() : LoginRepository {

    override suspend fun findUserByUsername(username: String): ResultRow? {
        return asyncDbQuery { UserEntity.select { UserEntity.username.eq(username) }.singleOrNull() }
    }

    override suspend fun updateToken(userId: Int, token: String): Int {
        return asyncDbQuery {
            UserEntity.update({ UserEntity.userId eq userId }) {
                it[UserEntity.token] = token
            }
        }
    }
}