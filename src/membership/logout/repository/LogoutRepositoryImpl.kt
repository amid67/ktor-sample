package com.raywenderlich.membership.logout.repository

import com.raywenderlich.extension.asyncDbQuery
import com.raywenderlich.membership.signup.v1.repository.UserEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class LogoutRepositoryImpl : LogoutRepository {

    override suspend fun findUserByToken(token: String): ResultRow? {
        return asyncDbQuery { UserEntity.select { UserEntity.token.eq(token) }.singleOrNull() }
    }

    override suspend fun clearToken(userId: Int): Int {
        return asyncDbQuery {
            UserEntity.update({ UserEntity.userId eq userId }) {
                it[token] = ""
            }
        }
    }
}
