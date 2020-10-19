package com.raywenderlich.membership.signup.v1.entity

import io.ktor.auth.*
import java.io.Serializable

data class User(
    val userId: Int,
    val username: String,
    val displayName: String? = null,
    var token: String? = null
) : Serializable, Principal