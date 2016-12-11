package ninja.luois.twicco.extension.observable

import rx.*

/**
 * Created by Abner on 15/11/20.
 */

// ObserveOf 使用方法
//
//  val ob: ObserveOf<String> = ObserveOf {
//      next { it -> }
//      error { e -> }
//      completed {  }
//  }

fun <T> observeNextOf(onNext: (T) -> Unit): ObserveNextOf<T> {
    return ObserveNextOf(onNext)
}

fun <T> observeOf(init: Observer<T>.() -> Unit): Observer<T> {
    return ObserveOf(init)
}

fun <T> observeCompletedOf(onCompleted: () -> Unit): ObserveCompletedOf<T> {
    return ObserveCompletedOf(onCompleted)
}

class ObserveOf<T>(init: ObserveOf<T>.() -> Unit) : Observer<T> {
    var next: ((T) -> Unit)? = null
    var completed: (() -> Unit)? = null
    var error: ((Throwable) -> Unit)? = null

    init {
        init(this)
    }

    override fun onCompleted() {
        this.completed?.invoke()
    }

    override fun onError(e: Throwable) {
        if (this.error != null) {
            this.error?.invoke(e)
        } else { throw e }
    }

    override fun onNext(t: T) {
        this.next?.invoke(t)
    }

    fun next(next: (T) -> Unit) {
        this.next = next
    }

    fun error(error: (Throwable) -> Unit) {
        this.error = error
    }

    fun completed(completed: () -> Unit) {
        this.completed = completed
    }
}

fun <T> Observable<T>.subscribeTo(init: ObserveOf<T>.() -> Unit): Subscription {
    return subscribe(ObserveOf(init))
}

class ObserveNextOf<T>(val onNext: (T) -> Unit) : Observer<T> {

    override fun onCompleted() {
    }

    override fun onNext(t: T) {
        this.onNext.invoke(t)
    }

    override fun onError(e: Throwable?) {
        if (e != null) { throw e }
    }
}

class ObserveCompletedOf<T>(val onCompleted: () -> Unit) : Observer<T> {
    override fun onError(e: Throwable?) {
        if (e != null) { throw e }
    }

    override fun onCompleted() {
        onCompleted.invoke()
    }

    override fun onNext(t: T) {
    }
}

fun <T> Single<T>.subscribeTo(init: SingleOf<T>.() -> Unit): Subscription {
    return this.subscribe(SingleOf(init))
}

class SingleOf<T>(init: SingleOf<T>.() -> Unit) : SingleSubscriber<T>() {
    var success: ((T) -> Unit)? = null
    var error: ((Throwable) -> Unit)? = null

    init {
        init(this)
    }

    override fun onSuccess(value: T) {
        success?.invoke(value)
    }

    override fun onError(tr: Throwable?) {
        error?.invoke(tr ?: Exception("Unknown error"))
    }

}
