## Domain layer design with [RxJava](https://github.com/ReactiveX/RxJava), [Kotlin](https://kotlinlang.org) and [Arrow](https://github.com/arrow-kt/arrow).
 
#### This project is about what kotlin and RxJava can give us in context of modeling domain use cases and error system in clean architecture approach.

It's intended for those already familiar with Clean Architecture approach. 
To familiarize yourself with the concept I recommend starting 
with these great posts.

1. [Android-CleanArchitecture](https://github.com/android10/Android-CleanArchitecture)
2. [applying-clean-architecture-on-android-hands-on](http://five.agency/android-architecture-part-4-applying-clean-architecture-on-android-hands-on/)

##### Lets recap the basic concepts of Clean Architecture (focusing on domain layer)

* Keeping the code clean with single responsibly principle.
* Isolation between layers: domain, data and presentation.
* Using the principle of Inversion of Control to make the domain independent from frameworks.

##### Some of the benefits of this approach:

* Easy change frameworks "Plug and play" 
* Easy share code between platform
* Fester tests

#### The project demonstrate simple domain layer with 4 use cases 
 
* [login](https://github.com/RubyLichtenstein/Domain-Layer-Modeling/blob/master/app/src/main/java/com/rubylich/cleanarchdomain/domain/usecase/LoginUseCase.kt)
* [logout](https://github.com/RubyLichtenstein/Domain-Layer-Modeling/blob/master/app/src/main/java/com/rubylich/cleanarchdomain/domain/usecase/LogoutUseCase.kt)
* [getPosts](https://github.com/RubyLichtenstein/Domain-Layer-Modeling/blob/master/app/src/main/java/com/rubylich/cleanarchdomain/domain/usecase/GetPostsUseCase.kt)
* [likePost](https://github.com/RubyLichtenstein/Domain-Layer-Modeling/blob/master/app/src/main/java/com/rubylich/cleanarchdomain/domain/usecase/LikePostUseCase.kt)

#### Modeling Use Cases With RxJava
Utilizing Reactive types, we'll demonstrate a Use Case Object, with and without a parameter.
  
##### Rx Reactive types   
* Observable  
* Single  
* Maybe
* Completable
* Flowable

### Use case structure
   
##### With parameter

T - the reactive type 

P - parameter type 

```kotlin
interface UseCaseWithParam<out T, in P> {

    fun build(param: P): T

    fun execute(param: P): T
}
```

##### Without parameter
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

### Modeling error system with Kotlin and [Arrow Either](http://arrow-kt.io/docs/datatypes/either/)  

#### The improvement to Rx error system 

1. Separation between expected and unexpected errors
2. Pattern matching for error state with kotlin sealed classes.
3. Keep the stream alive in case of expected errors, stop the stream only on unexpected or fatal errors.


The implementation is with Either stream Observable<Either<Error, Data>>
while Error is sealed class 
and the regular Rx onError is for unexpected errors only 
  
## Example

### Creating use case  
```kotlin
class SomeUseCase :
    ObservableWithParamUseCase<Either<SomeUseCase.Error, SomeUseCase.Data>, SomeUseCase.Param>(
        threadExecutor = TODO(),
        postExecutionThread = TODO()
    ) {

    override fun build(param: Param): Observable<Either<Error, Data>> {
        TODO()
    }

    data class Param(val hello: String)
    data class Data(val world: String)
    sealed class Error {
        object ErrorA : Error()
        object ErrorB : Error()
    }
}
```

### Consuming use case 
```kotlin
class SomePresenter(val someUseCase: SomeUseCase) {
    fun some() {
        someUseCase
            .execute(SomeUseCase.Param("Hello World!"))
            .subscribe(object : ObservableEitherObserver<SomeUseCase.Error, SomeUseCase.Data> {
                override fun onSubscribe(d: Disposable) = TODO()
                override fun onComplete() = TODO()
                override fun onError(e: Throwable) = onUnexpectedError(e)
                override fun onNextSuccess(r: SomeUseCase.Data) = showData(r)
                override fun onNextFailed(l: SomeUseCase.Error) = onFailed(
                    when (l) {
                        SomeUseCase.Error.ErrorA -> TODO()
                        SomeUseCase.Error.ErrorB -> TODO()
                    }
                )
            })
    }

    private fun onFailed(any: Any): Nothing = TODO()
    private fun showData(data: SomeUseCase.Data): Nothing = TODO()
    private fun onUnexpectedError(e: Throwable): Nothing = TODO()
}
```


#### Either Stream API
```kotlin
typealias Success<A, B> = Either.Right<A, B>

typealias Failed<A, B> = Either.Left<A, B>
```

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


