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



##Now let's dive in 
#### Modeling Use Cases With RxJava
Utilizing Reactive types, we'll demonstrate a Use Case Object, with and without a parameter.
  

### How to create use case 
Use case composed from
 
1. Reactive type - (Observable/Flowable/Single/Maybe/Completable)
2. Data (Optional) - The data witch use case will emit  
3. Error (Optional) - Expected use case error and will be sealed class
4. Parameter (Optional)

### Basic use case structure
   
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
### For each reactive type use case will look like: 

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
```kotlin
typealias MaybeWithoutParamUseCase<T> = UseCaseWithoutParam<Maybe<T>>
typealias MaybeWithParamUseCase<T, in P> = UseCaseWithParam<Maybe<T>, P>
```
#### Completable
```kotlin
typealias CompletableWithoutParamUseCase = UseCaseWithoutParam<Completable>
typealias CompletableWithParamUseCase<in P> = UseCaseWithParam<Completable, P>
```
#### Flowable
```kotlin
typealias FlowableWithoutParamUseCase<T> = UseCaseWithoutParam<Flowable<T>>
typealias FlowableWithParamUseCase<T, in P> = UseCaseWithParam<Flowable<T>, P>
```

## Use case example
#### Observable of either data or error and parameter 

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


### Modeling error system with Kotlin and [Arrow Either](http://arrow-kt.io/docs/datatypes/either/)  

#### The improvements to the regular rx error system 

1. Separation between expected and unexpected errors
2. Pattern matching for error state with kotlin sealed classes.
3. Keep the stream alive in case of expected errors, stop the stream only on unexpected or fatal errors.


The implementation is with Either stream Observable<Either<Error, Data>>
while Error is sealed class 
and the regular Rx onError is for unexpected errors only 


#### Creating either stream

You can create Either stream in one of the following ways  

1. Defining Either observable 
```kotlin
class CreateEither {
    fun create() {
      Observable.just<Either<Exception, String>>(Success("Hello"))
                .subscribe()
    }
}        
```

2. Converting regular stream to Either stream with this handy extension functions 

```kotlin

private fun <T> Observable<T>.toSuccess() = map { Success(it) }

private fun <T> Observable<T>.toFailure() = map { Failure(it) }

```
```kotlin
class CreateEither {
    fun toEither() {
        Observable.just("Hello Either")
            .toSuccess() 
        
        Observable.just("Hello Either")
            .toFailure()
  
    }
}
```

#### Operating on either stream

* Fold - applies `sucsess` block if this is a Success or `failure` if this is a Failure.

```kotlin
Observable.just<Either<Exception, String>>(Success("Hello"))
            .filter({ it.isRight() })
            .map { Failure(Exception()) }
            .fold({"on failure"},{"on success"})
```

#### Consuming either stream
```kotlin
someUseCase
    .execute(SomeUseCase.Param("Hello World!"))
    .subscribe(object : ObservableEitherObserver<SomeUseCase.Error, SomeUseCase.Data> {
        override fun onSubscribe(d: Disposable) = TODO()
        override fun onComplete() = TODO()
        override fun onError(e: Throwable) = onUnexpectedError(e)
        override fun onNextSuccess(r: SomeUseCase.Data) = showData(r)
        override fun onNextFailure(l: SomeUseCase.Error) = onFailure(
            when (l) {
                SomeUseCase.Error.ErrorA -> TODO()
                SomeUseCase.Error.ErrorB -> TODO()
            }
        )
    })
```
### Example of consuming use case and handling expect and unexpected errors separately  
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
                override fun onNextFailure(l: SomeUseCase.Error) = onFailure(
                    when (l) {
                        SomeUseCase.Error.ErrorA -> TODO()
                        SomeUseCase.Error.ErrorB -> TODO()
                    }
                )
            })
    }

    private fun onFailure(any: Any): Nothing = TODO()
    private fun showData(data: SomeUseCase.Data): Nothing = TODO()
    private fun onUnexpectedError(e: Throwable): Nothing = TODO()
}
```



