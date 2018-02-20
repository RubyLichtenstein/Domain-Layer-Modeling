package com.rubylich.cleanarchdomain.presentation

import com.rubylich.cleanarchdomain.domain.usecase.LogoutUseCase
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

/**
 * Created by rl98880 on 31/01/2018.
 */
class LogoutPresenter(private val logoutUseCase: LogoutUseCase) {
    fun logout() {
        logoutUseCase
            .execute()
            .subscribe(object : MaybeObserver<LogoutUseCase.Error> {
                override fun onComplete() {
                    showLogoutSuccess()
                }

                override fun onError(e: Throwable) {

                }

                override fun onSuccess(logoutError: LogoutUseCase.Error) {
                    val res = when (logoutError) {
                        LogoutUseCase.Error.LogoutError -> TODO()
                    }
                    showLogoutFail()
                }

                override fun onSubscribe(d: Disposable) {}
            })
    }

    private fun unknownErrorHandler() {}

    private fun showLogoutSuccess() {}
    private fun showLogoutFail() {}
}
