package com.raywenderlich

import com.raywenderlich.auth.JwtService
import org.koin.dsl.module

val appModules = module(createdAtStart = true){
    single { JwtService() }
}