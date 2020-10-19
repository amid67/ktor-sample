package com.raywenderlich.auth

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@KtorExperimentalAPI
val hashKey = hex(System.getenv("SECRET_KEY")) // 2

@KtorExperimentalAPI
val hmacKey = SecretKeySpec(hashKey, "HmacSHA1") // 3

@KtorExperimentalAPI
fun String.toHash(): String { // 4
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(toByteArray(Charsets.UTF_8)))
}