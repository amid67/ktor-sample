package com.raywenderlich.membership.login.v1.route

import com.raywenderlich.auth.JwtService
import com.raywenderlich.auth.MySession
import com.raywenderlich.auth.toHash
import com.raywenderlich.core.model.Respond
import com.raywenderlich.membership.MembershipConstants
import com.raywenderlich.membership.login.v1.entity.LoginReceiveRequest
import com.raywenderlich.membership.login.v1.repository.LoginRepository
import com.raywenderlich.membership.signup.v1.entity.User
import com.raywenderlich.membership.signup.v1.repository.UserEntity
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

@KtorExperimentalAPI
fun Route.login(
    loginRepository: LoginRepository,
    jwtService: JwtService) {
    post(MembershipConstants.Route.USER_LOGIN_API_V1) {
        val request = call.receive<LoginReceiveRequest>()
        val username = request.username
        val password = request.password
        when {
            username == null -> call.respond(
                HttpStatusCode.BadRequest,
                Respond.Error(HttpStatusCode.BadRequest.value, "Missing username field")
            )
            password == null -> call.respond(
                HttpStatusCode.BadRequest,
                Respond.Error(HttpStatusCode.BadRequest.value, "Missing password field")
            )
            else -> {
                val hash = password.toHash()
                val result = loginRepository.findUserByUsername(username)
                when {
                    result == null -> {
                    }
                    result[UserEntity.password] == hash -> {
                        val newToken = jwtService.generateToken(result[UserEntity.username])
                        loginRepository.updateToken(result[UserEntity.userId], newToken)
                        call.sessions.set(MySession(result[UserEntity.userId]))
                        call.respond(
                            HttpStatusCode.OK, Respond.Success(
                                User(
                                    userId = result[UserEntity.userId],
                                    username = result[UserEntity.username],
                                    token = newToken
                                )
                            )
                        )
                    }
                    else -> call.respond(
                        HttpStatusCode.Unauthorized,
                        Respond.Error(HttpStatusCode.Unauthorized.value, "input params is wrong")
                    )
                }
            }
        }
    }
}