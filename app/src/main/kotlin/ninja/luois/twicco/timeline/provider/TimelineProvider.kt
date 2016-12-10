package ninja.luois.twicco.timeline.provider

import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService
import rx.Observable
import rx.Subscriber
import rx.lang.kotlin.observable
import rx.schedulers.Schedulers

object TimelineProvider {
    val service: StatusesService
    val userName: String

    // all apply to io scheduler
    private fun <T> tlObservable(body: (s: Subscriber<in T>) -> Unit): Observable<T> {
        return observable<T> { body(it) }
                .subscribeOn(Schedulers.io())
    }

    init {
        //val session = TwitterCore.getInstance().sessionManager.activeSession!!
        service = TwitterCore.getInstance().apiClient.statusesService
        userName = TwitterCore.getInstance().sessionManager.activeSession.userName
    }

    fun homeTimeline_(): Observable<List<Tweet>> {
        return tlObservable { s ->
            try {
                val resp = service.homeTimeline(100, null, null, null, null, null, null)
                        .execute()
                if (resp.isSuccessful) {
                    s.onNext(resp.body())
                    s.onCompleted()
                } else {
                    s.onError(Exception(resp.errorBody().string()))
                }
            } catch (tr: Throwable) {
                s.onError(tr)
            }
        }
    }

    fun mentionTimeline_(): Observable<List<Tweet>> {
         return tlObservable { s ->
            try {
                val resp = service.mentionsTimeline(100, null, null, null, null, null)
                        .execute()
                if (resp.isSuccessful) {
                    s.onNext(resp.body())
                    s.onCompleted()
                } else {
                    s.onError(Exception(resp.errorBody().string()))
                }
            } catch (tr: Throwable) {
                s.onError(tr)
            }
        }
    }

    fun userTimeline_(screenName: String = userName): Observable<List<Tweet>> {
         return tlObservable { s ->
            try {
                val resp = service
                        .userTimeline(null, screenName, null,null,null,null,null,null, true)
                        .execute()

                if (resp.isSuccessful) {
                    s.onNext(resp.body())
                    s.onCompleted()
                } else {
                    s.onError(Exception(resp.errorBody().string()))
                }
            } catch (tr: Throwable) {
                s.onError(tr)
            }
        }
    }
}
























