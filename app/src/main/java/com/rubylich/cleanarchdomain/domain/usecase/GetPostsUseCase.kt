package com.rubylich.cleanarchdomain.domain.usecase

import arrow.core.Either
import com.rubylich.cleanarchdomain.domain.reposetory.PostRepository
import com.rubylich.cleanarchdomain.domain.services.UserService
import com.rubylich.cleanarchdomain.rxerror.Failure
import com.rubylich.cleanarchdomain.rxerror.Success
import com.rubylich.cleanarchdomain.rxusecase.ObservableWithoutParamUseCase
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
            : Observable<Either<Error, Data>> {

        return userService.getToken()
            .flatMapObservable {
                it.fold(
                    { Observable.just(Failure(Error.UserNotLogin)) },
                    { getPosts(postRepository, it) }
                )
            }
    }

    private fun getPosts(postRepository: PostRepository, token: String)
            : Observable<Either<Error, Data>>? {
        return postRepository.getPosts(token)
            .map {
                it.fold(
                    { Failure(Error.PostNotFound) },
                    { Success(Data(it.id, it.text)) }
                )
            }
    }

    sealed class Error {
        object NoNetwork : Error()
        object UserNotLogin : Error()
        object PostNotFound : Error()
    }

    data class Data(val id: String, var text: String)
}

