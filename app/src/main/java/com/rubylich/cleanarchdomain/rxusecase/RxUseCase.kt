package com.rubylich.cleanarchdomain.rxusecase


import io.reactivex.*

/**
 * Created by rl98880 on 28/01/2018.
 */

abstract class UseCase(
    private val threadExecutor: Scheduler,
    private val postExecutionThread: Scheduler
)

abstract class UseCaseWithoutParam<out T>(
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : UseCase(threadExecutor, postExecutionThread) {

    abstract fun build(): T

    abstract fun execute(): T
}

abstract class UseCaseWithParam<out T, in P>(
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : UseCase(threadExecutor, postExecutionThread) {

    abstract fun build(param: P): T

    abstract fun execute(param: P): T
}

abstract class CompletableWithParamUseCase<in P>(
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : UseCaseWithParam<Completable, P>(
    threadExecutor,
    postExecutionThread
)

abstract class CompletableUseCase(threadExecutor: Scheduler, postExecutionThread: Scheduler) :
    UseCaseWithoutParam<Completable>(
        threadExecutor,
        postExecutionThread
    )

abstract class ObservableWithParamUseCase<T, in P>(
    private val threadExecutor: Scheduler,
    private val postExecutionThread: Scheduler
) : UseCaseWithParam<Observable<T>, P>(
    threadExecutor,
    postExecutionThread
) {
    override fun execute(param: P): Observable<T> {
        return build(param)
            .subscribeOn(threadExecutor)
            .observeOn(postExecutionThread)
    }
}

abstract class ObservableWithoutParamUseCase<T>(
    private val threadExecutor: Scheduler,
    private val postExecutionThread: Scheduler
) : UseCaseWithoutParam<Observable<T>>(
    threadExecutor,
    postExecutionThread
) {
    override fun execute(): Observable<T> {
        return build()
            .subscribeOn(threadExecutor)
            .observeOn(postExecutionThread)
    }
}

abstract class SingleWithParamUseCase<T, in P>(
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : UseCaseWithParam<Single<T>, P>(
    threadExecutor,
    postExecutionThread
) {

}

abstract class SingleUseCase<T>(
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) :
    UseCaseWithoutParam<Single<T>>(
        threadExecutor,
        postExecutionThread
    )

abstract class MaybeWithParamUseCase<T, in P>(
    private val threadExecutor: Scheduler,
    private val postExecutionThread: Scheduler
) : UseCaseWithParam<Maybe<T>, P>(
    threadExecutor,
    postExecutionThread
) {

    override fun execute(param: P): Maybe<T> {
        return build(param)
            .subscribeOn(threadExecutor)
            .observeOn(postExecutionThread)
    }
}

abstract class MaybeUseCase<T>(
    private val threadExecutor: Scheduler,
    private val postExecutionThread: Scheduler
) : UseCaseWithoutParam<Maybe<T>>(
    threadExecutor,
    postExecutionThread
) {
    override fun execute(): Maybe<T> =
        build()
            .subscribeOn(threadExecutor)
            .observeOn(postExecutionThread)

}

abstract class FlowableWithParamUseCase<T, in P>(
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : UseCaseWithParam<Flowable<T>, P>(
    threadExecutor,
    postExecutionThread
)

abstract class FlowableUseCase<StreamT>(threadExecutor: Scheduler, postExecutionThread: Scheduler) :
    UseCaseWithoutParam<Flowable<StreamT>>(
        threadExecutor,
        postExecutionThread
    )
