package com.rubylich.cleanarchdomain

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rubylich.rl98880.cleanarchdomain.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}

//class ObservableKHK private constructor()
//typealias ObservableKKind<A> = arrow.HK<ObservableKHK, A>
//
//@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
//inline fun <A> ObservableKKind<A>.ev(): ObservableK<A> =
//    this as ObservableK<A>

//
//@higherkind
//data class ObservableK<A>(val observable: Observable<A>) :
//    ObservableKKind<A>
//
//@higherkind
//data class SingleK<A>(val single: Single<A>) : SingleKKind<A>
//
//@typeclass
//interface Schedul<F> : TC {
//    fun <T> observeOn(f: HK<F, T>, scheduler: Scheduler): HK<F, T>
//    fun <T> subscribeOn(f: HK<F, T>, scheduler: Scheduler): HK<F, T>
//}
//
//@instance(ObservableKHK::class)
//interface ObservableSchInstance<T> :
//    Schedul<ObservableKHK> {
//    override fun <T> observeOn(
//        f: HK<ObservableKHK, T>,
//        scheduler: Scheduler
//    ): HK<ObservableKHK, T> =
//        ObservableK(f.ev().observable.observeOn(scheduler))
//
//
//    override fun <T> subscribeOn(
//        f: HK<ObservableKHK, T>,
//        scheduler: Scheduler
//    ): HK<ObservableKHK, T> =
//        ObservableK(f.ev().observable.subscribeOn(scheduler))
//}

//@instance(SingleK::class)
//interface ObservableSchInstance<T> : Schedul<ObservableKHK> {
//    override fun <T> observeOn(
//        f: HK<ObservableKHK, T>,
//        scheduler: Scheduler
//    ): HK<ObservableKHK, T> =
//        ObservableK(f.ev().observable.observeOn(scheduler))
//
//
//    override fun <T> subscribeOn(
//        f: HK<ObservableKHK, T>,
//        scheduler: Scheduler
//    ): HK<ObservableKHK, T> =
//        ObservableK(f.ev().observable.subscribeOn(scheduler))
//}

//
//abstract class ListKHK private constructor() {
//    abstract fun <T> observeOn(
//        hk: HK<ListKHK, T>,
//        scheduler: Scheduler
//    ): HK<ListKHK, T>
//
//    abstract fun <T> subscribeOn(
//        hk: HK<ListKHK, T>,
//        scheduler: Scheduler
//    ): HK<ListKHK, T>
//
//}
//
//typealias ListKKind<A> = arrow.HK<ListKHK, A>
//typealias ListKKindedJ<A> = io.kindedj.Hk<ListKHK, A>
//
//@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
//inline fun <A> ListKKind<A>.ev(): ListK<A> =
//    this as ListK<A>
//
//@higherkind
//data class ListK<A>(val list: List<A>) : ListKKind<A>
//
//
//@typeclass
//interface Functor<F> : TC {
//    fun <A, B> map(fa: HK<F, A>, f: (A) -> B): HK<F, B>
//}
//
//@instance(ListK::class)
//interface ListKFunctorInstance : Functor<ListKHK> {
//    override fun <A, B> map(fa: HK<ListKHK, A>, f: (A) -> B): ListK<B> {
//        return fa.ev().list.map(f).k()
//    }
//}
//1
//class StreamHK private constructor() {}
//
//typealias StreamKind<T> = HK<StreamHK, T>
//class ObservableKHK private constructor()
//typealias ObservableKKind<T> = arrow.HK<ObservableKHK, T>
//typealias ObservableKKindedJ<T> = io.kindedj.Hk<ObservableKHK, T>
//
//@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
//inline fun <T> ObservableKKind<T>.ev(): ObservableK<T> =
//    this as ObservableK<T>

//@higherkind
//data class ObservableK<T>(val observable: Observable<T>)
//    : ObservableKKind<T>, Functor1<ObservableKKind<T>> by observable{
//    override fun <T> observeOn(
//        hk: ObservableKKind<T>,
//        scheduler: Scheduler
//    ): HK<ObservableKKind<T>, T> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun <T> subscribeOn(
//        hk: ObservableKKind<T>,
//        scheduler: Scheduler
//    ): HK<ObservableKKind<T>, T> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}
//


//
//@higherkind
//data class SequenceKW<out A> constructor(val sequence: Sequence<A>) : SequenceKWKind<A>,
//    Sequence<A> by sequence {
//
//}
//
//
//@instance(Observable::class)
//interface ListKFunctorInstance : Functor1<ObservableKHK> {
//    override fun <T> observeOn(
//        hk: ObservableKKind<T>,
//        scheduler: Scheduler
//    ): ObservableKKind<T> {
//        hk.ev().observable
//    }
//
//    override fun <T> subscribeOn(
//        hk: ObservableKKind<T>,
//        scheduler: Scheduler
//    ): ObservableKKind<T> {
//
//    }
//}
