package com.example.rl98880.cleanarchdomain.domain.usecase

import arrow.core.Either
import com.example.rl98880.cleanarchdomain.domain.reposetory.PostRepository
import com.example.rl98880.cleanarchdomain.domain.services.UserService
import com.example.rl98880.cleanarchdomain.rxerror.Failed
import com.example.rl98880.cleanarchdomain.rxerror.Success
import com.example.rl98880.cleanarchdomain.rxusecase.ObservableWithoutParamUseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by rl98880 on 03/02/2018.
 */
class GetPostsUseCase(
    private val postRepository: PostRepository,
    private val userService: UserService,
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : ObservableWithoutParamUseCase<Either<GetPostsUseCase.Error, GetPostsUseCase.Data>>(
        threadExecutor,
        postExecutionThread
) {

    override fun build()
            : Observable<Either<Error, GetPostsUseCase.Data>> {

        return userService.getToken()
            .flatMapObservable {
                it.fold(
                        { Observable.just(Failed(Error.NotValidToken)) },
                        { getPosts(postRepository, it) }
                )
            }.onErrorReturnItem(Failed(Error.Unknown))
    }

    private fun getPosts(postRepository: PostRepository, token: String)
            : Observable<Either<Error, GetPostsUseCase.Data>>? {
        return postRepository.getPosts(token)
            .map {
                it.fold(
                        { Failed(Error.PostNotFetched) },
                        { Success(Data(it.id, it.text)) }
                )
            }
    }

    sealed class Error {
        object Unknown : Error()
        object NotValidToken : Error()
        object PostNotFetched : Error()
    }

    data class Data(val id: String, var text: String)
}

