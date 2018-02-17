# Domain Modeling
Domain layer design with RxJava Kotlin and Kategory.

## Kotlin project demonstrate modeling use cases with Rx and error system with Kategory

#### Why Domain Driven Design - Clean Architecture?

* Keeping the code clean with Single Responsibly Principle
* Isolation between the layers domain, data and presentation.
* Independent Framework - inversion of control - plug and play
* Ease change implementation of services
* Share code between platform - platform independent
* Fest tests -  pure java, no framework related

#### Why Use Case?

1. Better understand our domain
2. Move logic to domain layer, presentation layer should have minimum logic.
3. Concise language and terms between teams platforms.

#### Why Rx

#### RxUseCase

1. Rx benefits (operators etc..)
1. Solid definition of use cases
2. Cross platform api with

Use case have data error, and may have parameter

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

Way Error system? 
#### RxError 

Problems 
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


