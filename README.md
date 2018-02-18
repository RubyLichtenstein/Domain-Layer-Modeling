# Domain layer design with RxJava Kotlin and Kategory.

I'ev desided to make this project becouse of 2 reasonse 

1. Show complete abstraction to domain use cases with all the rx reacitve type 
2. Show better error system in the rx stream and to use of power kotlin pattern matching to handle error states 


#### Why Domain Driven Design - Clean Architecture?

Its greate posts and project out theare  

1. 
2. 

I'am not foucusing of clean arch 
But a few points 

* Keeping the code clean with Single Responsibly Principle
* Isolation between the layers domain, data and presentation.
* Independent Framework - inversion of control - plug and play
* Ease change implementation of services
* Share code between platform - platform independent
* Fest tests -  pure java, no framework related
 
 
#### RxUseCase
Use case types

* Observable - Without/ParamUseCase
* Single - Without/ParamUseCase
* Maybe - Without/ParamUseCase
* Completable - Without/ParamUseCase
* Flowable - Without/ParamUseCase

#### With parameter

```kotlin
interface UseCaseWithParam<out T, in P> {

    abstract fun build(param: P): T

    abstract fun execute(param: P): T
}
```

#### Without parameter
```kotlin
interface UseCaseWithoutParam<out T> {

    abstract fun build(): T

    abstract fun execute(): T
}
```

#### Observable
```kotlin
typealias ObservableWithoutParamUseCase<T> = UseCaseWithoutParam<Observable<T>>
typealias ObservableWithParamUseCase<T, in P> = UseCaseWithParam<Observable<T>, P>
```

#### Single
```kotlin
typealias SingleWithoutParamUseCase<T> = UseCaseWithoutParam<Single<T>>
typealias SingleWithParamUseCase<T, in P> = UseCaseWithParam<Single<T>, P>
```

...

#### RxError 

What bad in error system 

1. Exceptions not passing threads, havey operation
2. infinity stream stop stream only if the stream is broken 
3. Pattern matching for error with seald classse 
4. Separation between expected and unexpected error

Sealed classes for branching errors

Either<Error, Data>
```kotlin
ObservableWithoutParamUseCase<Either<GetPostsUseCase.Error, GetPostsUseCase.Data>>
```
```kotlin
typealias Success<A, B> = Either.Right<A, B>

typealias Failed<A, B> = Either.Left<A, B>
```

#### Either Observer
```kotlin
interface EitherObserver<in L, in R> {
    fun onNextSuccess(r: R)
    fun onNextFailed(l: L)

    fun onEither(either: Either<L, R>) {
        either.fold(::onNextFailed, ::onNextSuccess)
    }
}

interface EitherSingleObserver<in L, in R> {
    fun onSuccess(r: R)
    fun onFailed(l: L)

    fun onEither(either: Either<L, R>) {
        either.fold(::onFailed, ::onSuccess)
    }
}
```


