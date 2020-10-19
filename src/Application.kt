package com.raywenderlich

import com.raywenderlich.auth.JwtService
import com.raywenderlich.auth.MySession
import com.raywenderlich.core.model.Respond
import com.raywenderlich.membership.di.membershipModule
import com.raywenderlich.membership.login.v1.repository.LoginRepository
import com.raywenderlich.membership.login.v1.route.login
import com.raywenderlich.membership.logout.repository.LogoutRepository
import com.raywenderlich.membership.logout.route.logout
import com.raywenderlich.membership.signup.v1.repository.SignupRepository
import com.raywenderlich.membership.signup.v1.route.signup
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.locations.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.sessions.*
import io.ktor.util.*
import org.koin.core.logger.Level
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger
import java.text.DateFormat
import javax.security.sasl.AuthenticationException

val port = System.getenv("PORT")?.toInt() ?: 23567

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused")
fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Koin) {
        slf4jLogger(Level.ERROR)
        modules(appModules, membershipModule)
    }
    DatabaseFactory.init()
    val signupRepository by inject<SignupRepository>()
    val loginRepository by inject<LoginRepository>()
    val logoutRepository by inject<LogoutRepository>()
    val jwtService by inject<JwtService>()

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "llTodo Server"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                when (val user = signupRepository.findUserByUsername(claim.asString())) {
                    is Respond.Success -> {
                        if (user.data.token.isNullOrEmpty()) {
                            null
                        } else {
                            user.data
                        }
                    }
                    else -> null
                }
            }
        }
    }
    install(Routing) {
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    Respond.Error(HttpStatusCode.Unauthorized.value, cause.localizedMessage)
                )
            }
            exception<Throwable> { e ->
                call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            }
        }
        signup(signupRepository, jwtService)
        login(loginRepository, jwtService)
        authenticate("jwt") {
            logout(logoutRepository)
        }
    }

}