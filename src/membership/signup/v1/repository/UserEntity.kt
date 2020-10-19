package com.raywenderlich.membership.signup.v1.repository

import org.jetbrains.exposed.sql.Table

object UserEntity : Table() {
    val userId = integer("id").autoIncrement().primaryKey()
    val username = varchar("username",256).uniqueIndex()
    val password= varchar("password",64)
    val token = varchar("token",256)
}