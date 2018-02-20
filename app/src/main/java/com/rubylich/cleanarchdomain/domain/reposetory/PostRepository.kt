package com.rubylich.cleanarchdomain.domain.reposetory

import arrow.core.Either
import io.reactivex.Maybe
import io.reactivex.Observable

/**
 * Created by rl98880 on 01/02/2018.
 */
interface PostRepository {
    fun getPosts(token: String): Observable<Either<FetchPostsError, Post>>
    fun likePost(id: String, token: String): Maybe<LikePostError>
    class Post(val id: String, val text: String)

    sealed class LikePostError
    sealed class FetchPostsError
}
