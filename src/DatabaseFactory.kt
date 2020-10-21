package com.raywenderlich

import com.raywenderlich.membership.signup.v1.repository.UserEntity
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    private lateinit var jdbcDriver:String
    private lateinit var jdbcDatabaseDriver:String
    private lateinit var dbUsername:String
    private lateinit var dbPassword:String

    fun init(jdbcDriver: String,jdbcDatabaseDriver: String, dbUsername: String,dbPassword: String) {
        this.jdbcDriver = jdbcDriver
        this.jdbcDatabaseDriver = jdbcDatabaseDriver
        this.dbUsername = dbUsername
        this.dbPassword = dbPassword
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(UserEntity)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = jdbcDriver
            jdbcUrl = jdbcDatabaseDriver
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            username = dbUsername
            password = dbPassword
        }
        config.validate()
        return HikariDataSource(config)
    }

}