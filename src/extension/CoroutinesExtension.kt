package com.raywenderlich.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction


suspend fun <T> asyncDbQuery(block: () -> T): T =
    withContext(Dispatchers.IO) {
        transaction { block() }
    }