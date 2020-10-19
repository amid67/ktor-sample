package com.raywenderlich.membership.login.v1.entity

data class LoginReceiveRequest(
    val username: String?,
    val password: String?
)