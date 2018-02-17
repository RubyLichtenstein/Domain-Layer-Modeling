package com.example.rl98880.cleanarchdomain.domain.services

import arrow.core.Either
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by rl98880 on 30/01/2018.
 */

interface AuthenticationService {
    fun login(loginData: LoginRequest): Single<Either<Error, String>>
    fun logOut(token: String): Maybe<Error>

    data class LoginRequest(
        val name: String,
        val password: String
    )

    sealed class Error {
        object NetworkError : Error()
        object EmailNotExist : Error()
        object WrongPassword : Error()
    }

}

