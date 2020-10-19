package com.raywenderlich.membership.di

import com.raywenderlich.membership.login.v1.repository.LoginRepository
import com.raywenderlich.membership.login.v1.repository.LoginRepositoryImpl
import com.raywenderlich.membership.logout.repository.LogoutRepository
import com.raywenderlich.membership.logout.repository.LogoutRepositoryImpl
import com.raywenderlich.membership.signup.v1.entity.UserEntityMapper
import com.raywenderlich.membership.signup.v1.repository.SignupRepository
import com.raywenderlich.membership.signup.v1.repository.SignupRepositoryImpl
import io.ktor.util.*
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

@KtorExperimentalAPI
val membershipModule = module {
    singleBy<SignupRepository, SignupRepositoryImpl>()
    singleBy<LoginRepository, LoginRepositoryImpl>()
    singleBy<LogoutRepository, LogoutRepositoryImpl>()
    single { UserEntityMapper() }
}