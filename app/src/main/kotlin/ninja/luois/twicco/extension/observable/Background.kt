package ninja.luois.twicco.extension.observable

import rx.Observable
import rx.Single
import rx.SingleSubscriber
import rx.Subscriber
import rx.lang.kotlin.observable
import rx.lang.kotlin.single
import rx.schedulers.Schedulers


// all apply to io scheduler
fun <T> bgObservable(body: (s: Subscriber<in T>) -> Unit): Observable<T> {
    return observable<T> { body(it) }
            .subscribeOn(Schedulers.io())
}

// all apply to io scheduler
fun <T> bgSingle(body: (s: SingleSubscriber<in T>) -> Unit): Single<T> {
    return single<T> { body(it) }
            .subscribeOn(Schedulers.io())
}
