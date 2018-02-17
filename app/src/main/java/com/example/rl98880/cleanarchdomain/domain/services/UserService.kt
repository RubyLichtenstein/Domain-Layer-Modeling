package com.example.rl98880.cleanarchdomain.domain.services

import arrow.core.Either
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by rl98880 on 03/02/2018.
 */
interface UserService {
    fun getToken(): Single<Either<Error, String>>
    fun setToken(token: String): Maybe<Error>

    sealed class Error {
        object TokenNotFound : Error()
        object CantSaveToken : Error()
    }
}


