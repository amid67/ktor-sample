package com.raywenderlich.membership.signup.v1.entity

import com.raywenderlich.core.model.Mapper
import com.raywenderlich.membership.signup.v1.repository.UserEntity
import org.jetbrains.exposed.sql.ResultRow

class UserEntityMapper: Mapper<ResultRow, User> {

    override fun mapFromInput(input: ResultRow): User {
        return  User(
            userId = input[UserEntity.userId],
            username = input[UserEntity.username],
            token = input[UserEntity.token]
        )
    }

    override fun mapToInput(output: User): ResultRow {
        TODO("Not yet implemented")
    }
}