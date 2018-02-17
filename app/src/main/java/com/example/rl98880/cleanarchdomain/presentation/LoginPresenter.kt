package com.example.rl98880.cleanarchdomain.presentation

import com.example.rl98880.cleanarchdomain.domain.usecase.LoginUseCase
import com.example.rl98880.cleanarchdomain.domain.usecase.LoginUseCase.Error.InvalidEmail
import com.example.rl98880.cleanarchdomain.domain.usecase.LoginUseCase.Error.InvalidPassword
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

/**
 * Created by rl98880 on 31/01/2018.
 */
class LoginPresenter(private val loginUseCase: LoginUseCase) {

    fun login(name: String, password: String) {
        loginUseCase
            .execute(LoginUseCase.Param(name, password))
            .subscribe(
                object : MaybeObserver<LoginUseCase.Error> {
                    override fun onSuccess(loginError: LoginUseCase.Error) {
                        val res = when (loginError) {
                            InvalidPassword -> showInvalidPasswordError()
                            InvalidEmail -> showInvalidEmailError()
                            LoginUseCase.Error.EmailNotExist -> TODO()
                            LoginUseCase.Error.WrongPassword -> TODO()
                            LoginUseCase.Error.LoginFailed -> TODO()
                        }
                    }

                    override fun onError(e: Throwable) {
                        onUnexpectedError(e)
                    }

                    override fun onSubscribe(d: Disposable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onComplete() {
                        showLoginSuccess()
                    }
                }
            )
    }

    private fun showInvalidEmailError() {

    }

    private fun showInvalidPasswordError() {

    }

    private fun onUnexpectedError(e: Throwable) {}
    private fun showLoginSuccess() {}
}
