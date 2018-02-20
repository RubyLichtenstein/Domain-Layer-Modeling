package com.rubylich.cleanarchdomain.domain.usecase

import com.rubylich.cleanarchdomain.domain.services.AuthenticationService
import com.rubylich.cleanarchdomain.domain.services.UserService
import com.rubylich.cleanarchdomain.rxusecase.MaybeUseCase
import io.reactivex.Maybe
import io.reactivex.Scheduler

/**
 * Created by rl98880 on 31/01/2018.
 */
class LogoutUseCase(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : MaybeUseCase<LogoutUseCase.Error>(threadExecutor, postExecutionThread) {
    override fun build(): Maybe<Error> {
        return userService.getToken()
            .flatMapMaybe {
                it.fold(
                    {
                        Maybe.just(
                            when (it) {
                                UserService.Error.TokenNotFound -> {
                                    Error.LogoutError
                                }
                                UserService.Error.CantSaveToken -> TODO()
                            }
                        )
                    },

                    { logout(it) }
                )
            }
    }

    private fun logout(it: String): Maybe<Error> {
        return authenticationService
            .logOut(it)
            .map { Error.LogoutError }
    }

    sealed class Error {
        object LogoutError : Error()
    }
}


