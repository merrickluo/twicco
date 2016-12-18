package ninja.luois.twicco.extension.observable

import rx.Observable
import rx.subjects.BehaviorSubject
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Variable<Element>(value: Element) {
    private val lock = ReentrantLock()
    private var _value: Element
    private val _subject: BehaviorSubject<Element>

    var value: Element
        get() {
            return lock.withLock {
                _value
            }
        }
        set(value) {
            lock.withLock {
                _value = value
            }
            _subject.onNext(value)
//            _subject.onCompleted()
        }

    fun asObservable(): Observable<Element> {
        return _subject.asObservable()
    }

    init {
        _value = value
        _subject = BehaviorSubject.create(value)
    }
}
