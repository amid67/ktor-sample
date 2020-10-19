package com.raywenderlich.membership.signup.v1.repository

import com.raywenderlich.core.model.Respond
import com.raywenderlich.extension.asyncDbQuery
import com.raywenderlich.membership.signup.v1.entity.UserEntityMapper
import com.raywenderlich.membership.signup.v1.entity.User
import io.ktor.http.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

class SignupRepositoryImpl(private val userEntityMapper: UserEntityMapper) : SignupRepository {

    override suspend fun findUserByUsername(username: String): Respond<User> {
        return asyncDbQuery {
            val result = UserEntity.select { UserEntity.username.eq(username) }
                .map { userEntityMapper.mapFromInput(it) }.singleOrNull()
            if (result == null) {
                Respond.Error(HttpStatusCode.NotFound.value, "user not found")
            } else {
                Respond.Success(result)
            }
        }
    }

    override suspend fun findUser(userId: Int): Respond<User> {
        return asyncDbQuery {
            val result = UserEntity.select { UserEntity.userId.eq(userId) }
                .map { userEntityMapper.mapFromInput(it) }.singleOrNull()
            if (result == null) {
                Respond.Error(-1, "database error")
            } else {
                Respond.Success(result)
            }
        }
    }

    override suspend fun addUser(username: String, passwordHash: String, jwtToken: String): Respond<User> {
        var statement: InsertStatement<Number>? = null
        asyncDbQuery {
            statement = UserEntity.insert { user ->
                user[UserEntity.username] = username
                user[password] = passwordHash
                user[token] = jwtToken
            }
        }
        val row = statement?.resultedValues?.get(0)
        return if (row == null) {
            Respond.Error(-1, "database error")
        } else {
            Respond.Success(userEntityMapper.mapFromInput(row))
        }
    }

}