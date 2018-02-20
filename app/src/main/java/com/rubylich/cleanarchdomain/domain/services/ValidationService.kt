package com.rubylich.cleanarchdomain.domain.services

import io.reactivex.Maybe

/**
 * Created by rl98880 on 03/02/2018.
 */
interface ValidationService {
    fun validateEmail(email: String): Maybe<Error>
    fun validatePassword(password: String): Maybe<Error>

    sealed class Error {
        sealed class InvalidEmail
        sealed class InvalidPassword
    }
}

