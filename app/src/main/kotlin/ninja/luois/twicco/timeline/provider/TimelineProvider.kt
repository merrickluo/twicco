package ninja.luois.twicco.timeline.provider

import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService
import ninja.luois.twicco.extension.observable.bgObservable
import rx.Observable

object TimelineProvider {
    val service: StatusesService
    val userName: String

    init {
        //val session = TwitterCore.getInstance().sessionManager.activeSession!!
        service = TwitterCore.getInstance().apiClient.statusesService
        userName = TwitterCore.getInstance().sessionManager.activeSession.userName
    }

    fun homeTimeline_(sinceId: Long? = null, maxId: Long? = null): Observable<List<Tweet>> {
        return bgObservable { s ->
            try {
                val resp = service.homeTimeline(100, sinceId, maxId, null, null, null, null)
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

    fun mentionTimeline_(sinceId: Long? = null, maxId: Long? = null): Observable<List<Tweet>> {
         return bgObservable { s ->
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

    fun userTimeline_(screenName: String = userName,
                      sinceId: Long? = null,
                      maxId: Long? = null): Observable<List<Tweet>> {
         return bgObservable { s ->
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
























