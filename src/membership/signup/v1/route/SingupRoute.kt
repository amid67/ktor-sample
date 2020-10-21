package com.raywenderlich.membership.signup.v1.route

import com.raywenderlich.auth.Auth
import com.raywenderlich.auth.JwtService
import com.raywenderlich.auth.MySession
import com.raywenderlich.core.model.Respond
import com.raywenderlich.membership.MembershipConstants
import com.raywenderlich.membership.signup.v1.entity.SignupReceiveRequest
import com.raywenderlich.membership.signup.v1.repository.SignupRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

@KtorExperimentalAPI
fun Route.signup(
    signupRepository: SignupRepository,
    jwtService: JwtService) {
    post(MembershipConstants.Route.USER_SIGNUP_API_V1) {
        val request = call.receive<SignupReceiveRequest>()
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
                val hash = Auth.convertToHash(password)
                try {
                    when (val resultFindUser = signupRepository.findUserByUsername(username)) {
                        is Respond.Success -> {
                            call.respond(
                                HttpStatusCode.Found,
                                Respond.Error(HttpStatusCode.Found.value, "This user before registered")
                            )
                        }
                        is Respond.Error -> {
                            val jwtToken = jwtService.generateToken(username)
                            when (val newUserResult = signupRepository.addUser(username, hash,jwtToken)) {
                                is Respond.Success -> {
                                    call.sessions.set(MySession(newUserResult.data.userId))
                                    call.respond(HttpStatusCode.Created, newUserResult)
                                }
                                is Respond.Error -> call.respond(
                                    HttpStatusCode.BadRequest,
                                    Respond.Error(HttpStatusCode.BadRequest.value, "Problem with create user")
                                )
                            }
                        }
                    }
                } catch (e: Throwable) {
                    application.log.error("Failed to register user", e)
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Respond.Error(HttpStatusCode.BadRequest.value, e.message.toString())
                    )
                }
            }
        }
    }
}
