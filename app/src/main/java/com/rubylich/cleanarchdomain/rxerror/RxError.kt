package com.rubylich.cleanarchdomain.rxerror

import arrow.core.Either
import io.reactivex.*

        /**
         * Created by rl98880 on 05/02/2018.
         */

typealias Success<A, B> = Either.Right<A, B>

typealias Failure<A, B> = Either.Left<A, B>

inline fun <A, B, C> Either<A, B>.fold1(
    crossinline failed: (A) -> C,
    crossinline success: (B) -> C
): C = this.fold(failed, success)

fun <T> Single<T>.toSuccess(): Single<Either<Nothing, T>> = this.map {
    Success(
        it
    )
}

fun <T> Single<T>.toFailure(): Single<Either<T, Nothing>> = this.map {
    Failure(
        it
    )
}

interface EitherObserver<in L, in R> {
    fun onNextSuccess(r: R)
    fun onNextFailure(l: L)

    fun onEither(either: Either<L, R>) {
        either.fold(::onNextFailure, ::onNextSuccess)
    }
}

interface EitherSingleObserver<in L, in R> {
    fun onSuccess(r: R)
    fun onFailure(l: L)

    fun onEither(either: Either<L, R>) {
        either.fold(::onFailure, ::onSuccess)
    }
}

interface ObservableEitherObserver<L, R>
    : Observer<Either<L, R>>, EitherObserver<L, R> {
    override fun onNext(t: Either<L, R>) = onEither(t)
}

interface FlowableEitherSubscriber<L, R>
    : FlowableSubscriber<Either<L, R>>,
    EitherObserver<L, R> {
    override fun onNext(t: Either<L, R>) = onEither(t)
}

interface MaybeEitherObserver<L, R>
    : MaybeObserver<Either<L, R>>,
    EitherSingleObserver<L, R> {
    override fun onSuccess(t: Either<L, R>) = onEither(t)
}

interface SingleEitherObserver<L, R>
    : SingleObserver<Either<L, R>>,
    EitherSingleObserver<L, R> {
    override fun onSuccess(t: Either<L, R>) = onEither(t)
}


