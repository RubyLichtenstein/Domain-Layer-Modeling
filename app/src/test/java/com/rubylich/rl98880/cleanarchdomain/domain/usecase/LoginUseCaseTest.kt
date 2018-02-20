package com.rubylich.rl98880.cleanarchdomain.domain.usecase

import com.rubylich.cleanarchdomain.domain.services.AuthenticationService
import com.rubylich.cleanarchdomain.domain.services.UserService
import com.rubylich.cleanarchdomain.domain.services.ValidationService
import com.nhaarman.mockito_kotlin.mock
import com.rubylich.cleanarchdomain.domain.usecase.LoginUseCase
import com.rubylichtenstein.rxtest.assertions.should
import com.rubylichtenstein.rxtest.extentions.test
import com.rubylichtenstein.rxtest.matchers.complete
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by rl98880 on 11/02/2018.
 */
class LoginUseCaseTest {
    /* Given */
    val authenticationServiceMock = mock<AuthenticationService> {
    }

    val validationServiceMock = mock<ValidationService> {
    }

    val userServiceMock = mock<UserService> {
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    val loginUseCase: LoginUseCase =
        LoginUseCase(
            authenticationServiceMock,
            validationServiceMock,
            userServiceMock,
            threadExecutor = Schedulers.trampoline(),
            postExecutionThread = Schedulers.trampoline()
        )

    @Test
    fun buildUseCase_email_not_valid() {
        val token = "some_token"
        Mockito.`when`(validationServiceMock.validateEmail(Mockito.anyString()))
            .thenReturn(Maybe.empty())

        Mockito.`when`(validationServiceMock.validatePassword(Mockito.anyString()))
            .thenReturn(Maybe.empty())

        Mockito.`when`(authenticationServiceMock.login(Mockito.any()))
            .thenReturn(Single.just(com.rubylich.cleanarchdomain.rxerror.Success(token)))

        Mockito.`when`(userServiceMock.setToken(token))
            .thenReturn(Maybe.empty())

        loginUseCase.execute(LoginUseCase.Param("", "ruby"))
            .test {
                it should complete()
            }
    }

}