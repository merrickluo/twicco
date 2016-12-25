package ninja.luois.twicco.timeline.provider

import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService
import ninja.luois.twicco.extension.observable.Variable
import ninja.luois.twicco.extension.observable.bgObservable
import ninja.luois.twicco.extension.observable.bgSingle
import rx.Observable
import rx.Single

object TimelineProvider {
    private val service: StatusesService
    private val userName: String

    private fun <T> tlObservable(body: (() -> Pair<T?, Exception?>)): Observable<T> {
        return bgObservable<T> { s ->
            try {
                val r = body()
                r.first?.let {
                    s.onNext(it)
                }
                r.second?.let {
                    s.onError(it)
                }
                s.onCompleted()
            } catch (e: Throwable) {
                s.onError(e)
            }
        }
    }

    private fun <T> tlSingle(body: (() -> Pair<T?, Exception?>)): Single<T> {
        return bgSingle<T> { s ->
            try {
                val r = body()
                r.first?.let {
                    s.onSuccess(it)
                }
                r.second?.let {
                    s.onError(it)
                }
            } catch (e: Throwable) {
                s.onError(e)
            }
        }
    }

    init {
        //val session = TwitterCore.getInstance().sessionManager.activeSession!!
        service = TwitterCore.getInstance().apiClient.statusesService
        userName = TwitterCore.getInstance().sessionManager.activeSession.userName
    }

    fun homeTimeline_(sinceId: Long? = null, maxId: Long? = null): Observable<List<Tweet>> {
        return tlObservable {
            val resp = service
                    .homeTimeline(100, sinceId, maxId, null, null, null, null)
                    .execute()
            if (resp.isSuccessful) {
                resp.body() to null
            } else {
                null to Exception(resp.errorBody().string())
            }
        }
    }

    fun mentionTimeline_(sinceId: Long? = null, maxId: Long? = null): Observable<List<Tweet>> {
         return tlObservable {
             val resp = service
                     .mentionsTimeline(100, sinceId, maxId, null, null, null)
                     .execute()
             if (resp.isSuccessful) {
                 resp.body() to null
             } else {
                 null to Exception(resp.errorBody().string())
             }
         }
    }

    fun userTimeline_(screenName: String = userName,
                      sinceId: Long? = null,
                      maxId: Long? = null): Observable<List<Tweet>> {
        return tlObservable {
            val resp = service
                    .userTimeline(null, screenName, 100, sinceId, maxId, null, null, null, true)
                    .execute()

            if (resp.isSuccessful) {
                resp.body() to null
            } else {
                null to Exception(resp.errorBody().string())
            }
        }
    }

    fun tweet_(id: Long): Observable<Tweet> {
        return tlObservable {
            val resp = service
                    .show(id, null, null, null)
                    .execute()
            if (resp.isSuccessful) {
                resp.body() to null
            } else {
                null to Exception(resp.errorBody().string())
            }
        }
    }

    fun retweet_(tweetId: Long): Single<Tweet> {
        return tlSingle {
            val resp = service.retweet(tweetId, null).execute()
            if (resp.isSuccessful) {
                resp.body() to null
            } else {
                null to Exception(resp.errorBody().string())
            }
        }
    }

    fun unretweet_(tweetId: Long): Single<Tweet> {
        return tlSingle {
            val resp = service.unretweet(tweetId, null).execute()
            if (resp.isSuccessful) {
                resp.body() to null
            } else {
                null to Exception(resp.errorBody().string())
            }
        }
    }

    fun heart_(tweetId: Long): Single<Tweet> {
         return tlSingle {
             val resp = TwitterCore.getInstance()
                     .apiClient.favoriteService.create(tweetId, true)
                     .execute()
             if (resp.isSuccessful) {
                 resp.body() to null
             } else {
                 null to Exception(resp.errorBody().string())
             }
         }
    }

    fun unheart_(tweetId: Long): Single<Unit> {
        return tlSingle {
             val resp = TwitterCore.getInstance()
                     .apiClient.favoriteService.destroy(tweetId, true)
                     .execute()
             if (resp.isSuccessful) {
                 Unit to null
             } else {
                 null to Exception(resp.errorBody().string())
             }
         }
    }
}
























