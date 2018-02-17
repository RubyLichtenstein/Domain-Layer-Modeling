package com.example.rl98880.cleanarchdomain.domain.usecase

import com.example.rl98880.cleanarchdomain.domain.reposetory.PostRepository
import com.example.rl98880.cleanarchdomain.domain.services.UserService
import com.example.rl98880.cleanarchdomain.rxusecase.MaybeWithParamUseCase
import io.reactivex.Maybe
import io.reactivex.Scheduler

/**
 * Created by rl98880 on 03/02/2018.
 */
class LikePostUseCase(
    private val postRepository: PostRepository,
    private val userService: UserService,
    threadExecutor: Scheduler,
    postExecutionThread: Scheduler
) : MaybeWithParamUseCase<LikePostUseCase.Error, String>(
    threadExecutor,
    postExecutionThread
) {

    override fun build(param: String): Maybe<LikePostUseCase.Error> {
        return userService.getToken()
            .flatMapMaybe {
                it.fold(
                    { Maybe.just(Error.NoUserToken) },
                    { likePost(param, it) }
                )
            }
    }

    private fun likePost(
        params: String,
        it: String
    ): Maybe<Error.LikeFail> {
        return postRepository.likePost(params, it)
            .map { Error.LikeFail }
    }

    sealed class Error {
        object NoUserToken : Error()
        object LikeFail : Error()
    }
}
