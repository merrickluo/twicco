package ninja.luois.twicco.timeline.provider

import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.*
import com.twitter.sdk.android.core.services.StatusesService
import io.realm.Realm
import io.realm.RealmList
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
            val ts = Realm.getDefaultInstance()
                    .where(ninja.luois.twicco.common.model.Tweet::class.java)
                    .findAll()
            val resp = service
                    .homeTimeline(100, sinceId, maxId, null, null, null, null)
                    .execute()
            if (resp.isSuccessful) {
                resp.body() to null
            } else {
                null to Exception(resp.errorBody().string())
            }
        }.doOnNext { saveToDB(it) }
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

    private fun saveToDB(tweets: List<Tweet>) {
        Realm.getDefaultInstance().executeTransaction { realm ->
            try {
                tweets.map(Tweet::toDBTweet)
                        .forEach {
                            realm.insertOrUpdate(it)
                        }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}

fun Coordinates.toDBCoordinates(): ninja.luois.twicco.common.model.Coordinates {
    return ninja.luois.twicco.common.model.Coordinates(
            this.longitude,
            this.latitude,
            this.type
    )
}

fun UrlEntity.toDBEntity(): ninja.luois.twicco.common.model.UrlEntity {
    return ninja.luois.twicco.common.model.UrlEntity(
            this.url,
            this.expandedUrl,
            this.displayUrl,
            this.start,
            this.end
    )
}

fun MentionEntity.toDBEntity(): ninja.luois.twicco.common.model.MentionEntity {
    return ninja.luois.twicco.common.model.MentionEntity(
            this.id,
            this.idStr,
            this.name,
            this.screenName,
            this.start,
            this.end
    )
}

fun MediaEntity.toDBEntity(): ninja.luois.twicco.common.model.MediaEntity {
    return ninja.luois.twicco.common.model.MediaEntity(
            this.id,
            this.idStr,
            this.mediaUrl,
            this.mediaUrlHttps,
            this.sourceStatusId,
            this.type,
            this.altText,
            this.start,
            this.end
    )
}

fun HashtagEntity.toDBEntity(): ninja.luois.twicco.common.model.HashtagEntity {
    return ninja.luois.twicco.common.model.HashtagEntity(
            this.text,
            this.start,
            this.end
    )
}

fun TweetEntities.toDBEntities(): ninja.luois.twicco.common.model.TweetEntities {
    val urls = this.urls?.let {
        RealmList(*(it.map { it.toDBEntity() }.toTypedArray()))
    } ?: RealmList()
    val mentions = this.userMentions?.let {
        RealmList(*(it.map { it.toDBEntity() }.toTypedArray()))
    } ?: RealmList()
    val media = this.media?.let {
        RealmList(*(it.map { it.toDBEntity() }.toTypedArray()))
    } ?: RealmList()
    val hashtags = this.hashtags?.let {
        RealmList(*(it.map { it.toDBEntity() }.toTypedArray()))
    } ?: RealmList()

    return ninja.luois.twicco.common.model.TweetEntities(
            urls,
            mentions,
            media,
            hashtags
    )
}

fun User.toDBUser(): ninja.luois.twicco.common.model.User {
    return ninja.luois.twicco.common.model.User(
            this.contributorsEnabled,
            this.createdAt,
            this.defaultProfile,
            this.defaultProfileImage,
            this.description,
            this.email,
            //this.entities,
            this.favouritesCount,
            this.followRequestSent,
            this.followersCount,
            this.friendsCount,
            this.geoEnabled,
            this.id,
            this.idStr,
            this.isTranslator,
            this.lang,
            this.listedCount,
            this.location,
            this.name,
            this.profileBackgroundColor,
            this.profileBackgroundImageUrl,
            this.profileBackgroundImageUrlHttps,
            this.profileBackgroundTile,
            this.profileBannerUrl,
            this.profileImageUrl,
            this.profileImageUrlHttps,
            this.profileLinkColor,
            this.profileSidebarBorderColor,
            this.profileSidebarFillColor,
            this.profileTextColor,
            this.profileUseBackgroundImage,
            this.protectedUser,
            this.screenName,
            this.showAllInlineMedia,
            this.status?.toDBTweet(),
            this.statusesCount,
            this.timeZone,
            this.url,
            this.utcOffset,
            this.verified,
            RealmList(),
            this.withheldScope

    )
}

fun Tweet.toDBTweet(): ninja.luois.twicco.common.model.Tweet {
    return ninja.luois.twicco.common.model.Tweet(
            this.id,
            this.idStr,
            this.coordinates?.toDBCoordinates(),
            this.createdAt,
            this.entities?.toDBEntities(),
            this.extendedEtities?.toDBEntities(),
            this.favoriteCount,
            this.favorited,
            this.filterLevel,
            this.inReplyToScreenName,
            this.inReplyToStatusId,
            this.inReplyToStatusIdStr,
            this.inReplyToUserId,
            this.inReplyToUserIdStr,
            this.lang,
            this.possiblySensitive,
            this.quotedStatusId,
            this.quotedStatusIdStr,
            this.quotedStatus?.toDBTweet(),
            this.retweetCount,
            this.retweeted,
            this.retweetedStatus?.toDBTweet(),
            this.source,
            this.text,
            this.displayTextRange[0],
            this.displayTextRange[1],
            this.truncated,
            this.user?.toDBUser(),
            this.withheldCopyright,
            RealmList(),
            this.withheldScope
    )
}
























