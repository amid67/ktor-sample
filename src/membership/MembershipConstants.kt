package com.raywenderlich.membership

object MembershipConstants {

    object Version{
        const val API_V1 = "/v1"
    }

    object Route{
        private const val USERS_API_V1 = "${Version.API_V1}/users"
        const val USER_LOGIN_API_V1 = "$USERS_API_V1/login"
        const val USER_SIGNUP_API_V1 = "$USERS_API_V1/signup"
        const val USER_LOGOUT_API_V1 = "$USERS_API_V1/logout"
    }

}