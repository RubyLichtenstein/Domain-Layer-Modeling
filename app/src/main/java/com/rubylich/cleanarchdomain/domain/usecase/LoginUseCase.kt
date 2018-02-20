package com.rubylich.cleanarchdomain.domain.usecase

import com.rubylich.cleanarchdomain.domain.services.AuthenticationService
import com.rubylich.cleanarchdomain.domain.services.UserService
import com.rubylich.cleanarchdomain.domain.services.ValidationService
import com.rubylich.cleanarchdomain.rxusecase.MaybeWithParamUseCase
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by rl98880 on 30/01/2018.
 */

class LoginUseCase(
    private val authenticationService: AuthenticationService,
    private val validationService: ValidationService,
    private val userService: UserService,
    threadExecutor: Scheduler = Schedulers.io(),
    postExecutionThread: Scheduler = Schedulers.io()
) : MaybeWithParamUseCase<LoginUseCase.Error, LoginUseCase.Param>(
    threadExecutor,
    postExecutionThread
) {

    override fun build(param: Param)
            : Maybe<Error> {
        return with(param) {
            Maybe.concat(
                validationService.validateEmail(email)
                    .map { (Error.InvalidEmail) },

                validationService.validatePassword(password)
                    .map { (Error.InvalidPassword) },

                authenticationService
                    .login(AuthenticationService.LoginRequest(email, password))
                    .flatMapMaybe {
                        it.fold(
                            {
                                Maybe.just(
                                    when (it) {
                                        AuthenticationService.Error.EmailNotExist -> Error.EmailNotExist
                                        AuthenticationService.Error.WrongPassword -> Error.WrongPassword
                                        AuthenticationService.Error.NetworkError  -> Error.LoginFailed
                                    }
                                )
                            },
                            { setToken(userService, it) }
                        )
                    }
            ).firstElement()
        }
    }

    private fun setToken(userService: UserService, token: String)
            : Maybe<Error> {
        return userService.setToken(token)
            .map { Error.LoginFailed }
    }

    sealed class Error {
        object InvalidEmail : Error()
        object InvalidPassword : Error()
        object EmailNotExist : Error()
        object WrongPassword : Error()
        object LoginFailed : Error()
    }

    data class Param(val email: String, val password: String)
}




