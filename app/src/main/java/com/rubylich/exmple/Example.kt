package com.rubylich.exmple

import arrow.core.Either
import com.rubylich.cleanarchdomain.rxerror.Failure
import com.rubylich.cleanarchdomain.rxerror.ObservableEitherObserver
import com.rubylich.cleanarchdomain.rxerror.Success
import com.rubylich.cleanarchdomain.rxusecase.ObservableWithParamUseCase
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by rl98880 on 19/02/2018.
 */
class CreateEither {
    fun toEither() {
        Observable.just("Hello Either")
            .toSuccess()

        Observable.just<Either<String, String>>(Success("Hello Either"))
            .onErrorReturnItem(Failure("sh*t"))

        Observable.just<Either<Exception, String>>(Success("Hello"))
            .filter({ it.isRight() })
            .map { Failure(Exception()) }
            .fold({"on failure"},{"code on success"})


    }
}

private inline fun <A, B, C> Observable<Either<A, B>>.fold(
    crossinline fa: (A) -> C,
    crossinline fb: (B) -> C
): Observable<C> = map { it.fold(fa, fb) }

private fun <T> Observable<T>.toSuccess() = map { Success(it) }

private fun <T> Observable<T>.toFailure() = map { Failure(it) }


class SomeUseCase :
    ObservableWithParamUseCase<Either<SomeUseCase.Error, SomeUseCase.Data>, SomeUseCase.Param>(
        threadExecutor = Schedulers.io(),
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
            .subscribe(object :
                ObservableEitherObserver<SomeUseCase.Error, SomeUseCase.Data> {
                override fun onSubscribe(d: Disposable) = TODO()
                override fun onComplete() = TODO()
                override fun onError(e: Throwable) = onUnexpectedError(e)
                override fun onNextSuccess(r: SomeUseCase.Data) = showData(r)
                override fun onNextFailure(l: SomeUseCase.Error) = onFailed(
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
