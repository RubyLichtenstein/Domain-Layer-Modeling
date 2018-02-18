# Domain layer design with RxJava, Kotlin and Kategory.

The project focus on 2 aspects of Domain layer architecture
   
1. Domain use cases modeling with RxJava 
2. Enhanced error system for rx stream with the power of kotlin pattern matching to handle error states 


#### Why Domain Driven Design - Clean Architecture?

Its great posts and project out there  

1. https://github.com/android10/Android-CleanArchitecture
2. http://five.agency/android-architecture-part-4-applying-clean-architecture-on-android-hands-on/

But a few points about the befits of CleanArchitecture with focus on domain layer 

* Keeping the code clean with Single Responsibly Principle
* Isolation between the layers domain, data and presentation.
* Independent Framework - inversion of control - plug and play
* Ease change implementation of services
* Share code between platform - platform independent
* Fest tests - pure java, no framework related

#### RxUseCase
Use cases type can be one of reactive types
  
* Observable - Without/ParamUseCase
* Single - Without/ParamUseCase
* Maybe - Without/ParamUseCase
* Completable - Without/ParamUseCase
* Flowable - Without/ParamUseCase

#### With parameter

T - the reactive type 

P - parameter type 

```kotlin
interface UseCaseWithParam<out T, in P> {

    fun build(param: P): T

    fun execute(param: P): T
}
```

#### Without parameter
```kotlin
interface UseCaseWithoutParam<out T> {

    fun build(): T

    fun execute(): T
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

#### Maybe
#### Completable
#### Flowable

...

#### RxError 

What is missing in rx error system 


1. Separation between expected and unexpected errors
2. Pattern matching for error state with sealed classes
3. I want the stream to stop only on unexpected and fatal errors, not in expected error    

The solution is Either stream Observable<Either<Error, Data>>
and Error is sealed class 
The regular on error is for unexpected errors only 

 
Some handy 
 
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


