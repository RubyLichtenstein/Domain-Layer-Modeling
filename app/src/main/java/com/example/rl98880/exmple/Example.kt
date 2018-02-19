package com.example.rl98880.exmple

import arrow.core.Either
import com.example.rl98880.cleanarchdomain.rxerror.ObservableEitherObserver
import com.example.rl98880.cleanarchdomain.rxusecase.ObservableWithParamUseCase
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by rl98880 on 19/02/2018.
 */

class SomeUseCase :
    ObservableWithParamUseCase<Either<SomeUseCase.Error, SomeUseCase.Data>, SomeUseCase.Param>(
        threadExecutor = TODO(),
        postExecutionThread = TODO()
    ) {

    override fun build(param: Param): Observable<Either<Error, Data>> {
        TODO()
    }

    data class Param(val hello: String)
    data class Data(val world: String)
    sealed class Error {
        object ErrorA : Error()
        object ErrorB : Error()
    }
}

class SomePresenter(val someUseCase: SomeUseCase) {
    fun some() {
        someUseCase
            .execute(SomeUseCase.Param("Hello World!"))
            .subscribe(object : ObservableEitherObserver<SomeUseCase.Error, SomeUseCase.Data> {
                override fun onSubscribe(d: Disposable) = TODO()
                override fun onComplete() = TODO()
                override fun onError(e: Throwable) = onUnexpectedError(e)
                override fun onNextSuccess(r: SomeUseCase.Data) = showData(r)
                override fun onNextFailed(l: SomeUseCase.Error) = onFailed(
                    when (l) {
                        SomeUseCase.Error.ErrorA -> TODO()
                        SomeUseCase.Error.ErrorB -> TODO()
                    }
                )
            })
    }

    private fun onFailed(any: Any): Nothing = TODO()
    private fun showData(data: SomeUseCase.Data): Nothing = TODO()
    private fun onUnexpectedError(e: Throwable): Nothing = TODO()
}
