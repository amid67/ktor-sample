package com.raywenderlich.membership.logout.route

import com.raywenderlich.auth.MySession
import com.raywenderlich.core.model.Respond
import com.raywenderlich.membership.MembershipConstants
import com.raywenderlich.membership.logout.entity.LogoutReceiveRequest
import com.raywenderlich.membership.logout.repository.LogoutRepository
import com.raywenderlich.membership.signup.v1.repository.UserEntity
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.logout(
    logoutRepository: LogoutRepository
) {
    post(MembershipConstants.Route.USER_LOGOUT_API_V1) {
        val token = call.receive<LogoutReceiveRequest>().token
        if (token.isNullOrEmpty()) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                Respond.Error(HttpStatusCode.BadRequest.value, "token requirement")
            )
        }
        when (val user = logoutRepository.findUserByToken(token)) {
            null -> call.respond(
                HttpStatusCode.NotFound,
                Respond.Error(HttpStatusCode.NotFound.value, "user not found")
            )
            else -> {
                logoutRepository.clearToken(user[UserEntity.userId])
                call.sessions.clear(call.sessions.findName(MySession::class))
                call.respond(HttpStatusCode.OK, Respond.Success(HttpStatusCode.OK.value))
            }
        }
    }
}