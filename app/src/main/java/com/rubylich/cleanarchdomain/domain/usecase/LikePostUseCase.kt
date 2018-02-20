package com.rubylich.cleanarchdomain.domain.usecase

import com.rubylich.cleanarchdomain.domain.reposetory.PostRepository
import com.rubylich.cleanarchdomain.domain.services.UserService
import com.rubylich.cleanarchdomain.rxusecase.MaybeWithParamUseCase
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

    override fun build(param: String): Maybe<Error> {
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
