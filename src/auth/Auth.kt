package com.raywenderlich.auth

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Auth {

    private lateinit var secretKey: String
    fun init(secretKey: String){
        this.secretKey = secretKey
    }

    @KtorExperimentalAPI
    private val hashKey = hex("898748674728934843")

    @KtorExperimentalAPI
    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

    @KtorExperimentalAPI
    fun convertToHash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }
}
