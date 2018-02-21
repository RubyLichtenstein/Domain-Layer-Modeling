package com.rubylich.cleanarchdomain.presentation

import com.rubylich.cleanarchdomain.domain.usecase.GetPostsUseCase
import com.rubylich.cleanarchdomain.domain.usecase.LikePostUseCase
import com.rubylich.cleanarchdomain.rxerror.ObservableEitherObserver
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

/**
 * Created by rl98880 on 03/02/2018.
 */
class FeedPresenter(
    private val fetchPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikePostUseCase
) {
    fun getPosts() {
        fetchPostsUseCase.execute()
            .subscribe(object :
                ObservableEitherObserver<GetPostsUseCase.Error, GetPostsUseCase.Data> {
                override fun onComplete() {

                }

                override fun onError(e: Throwable) {

                }

                override fun onNextSuccess(r: GetPostsUseCase.Data) {
                    showPost(r)
                }

                override fun onNextFailure(l: GetPostsUseCase.Error) {
                    val error = when (l) {
                        GetPostsUseCase.Error.Unknown       -> TODO()
                        GetPostsUseCase.Error.NotValidToken -> TODO()
                        GetPostsUseCase.Error.PostNotFound  -> TODO()
                    }
                    showFetchPostError()
                }

                override fun onSubscribe(d: Disposable) {

                }
            }
            )
    }

    fun likePost(id: String) {
        likePostUseCase.execute(id)
            .subscribe(
                object : MaybeObserver<LikePostUseCase.Error> {
                    override fun onComplete() {
                        showLikeSuccess()
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onSuccess(logoutError: LikePostUseCase.Error) {
                        val res = when (logoutError) {
                            LikePostUseCase.Error.NoUserToken -> TODO()
                            LikePostUseCase.Error.LikeFail    -> TODO()
                        }
                        showLikeFailed()
                    }

                    override fun onSubscribe(d: Disposable) {}
                }
            )
    }

    private fun showLikeFailed() {

    }

    private fun showLikeSuccess() {

    }

    private fun showFetchPostError() {

    }

    private fun showPost(it: GetPostsUseCase.Data) {

    }
}