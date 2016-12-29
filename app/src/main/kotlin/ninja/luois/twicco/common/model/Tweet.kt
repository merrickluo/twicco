package ninja.luois.twicco.common.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Coordinates(
        open var longitude: Double? = null,
        open var latitude: Double? = null,
        open var type: String? = null
) : RealmObject()

open class UrlEntity (
        open var url: String? = null,
        open var expandedUrl: String? = null,
        open var displayUrl: String? = null,
        open var start: Int = 0,
        open var end: Int = 0
) : RealmObject()

open class MentionEntity (
        open var id: Long? = null,
        open var idStr: String? = null,
        open var name: String? = null,
        open var screenName: String? = null,
        open var start: Int = 0,
        open var end: Int = 0
) : RealmObject()

//open class Sizes(
//
//) : RealmObject()
//
//open class Size(
//
//) : RealmObject()
//
//open class VideoInfo(
//
//) : RealmObject()

open class MediaEntity (
        open var id: Long? = null,
        open var idStr: String? = null,
        open var mediaUrl: String? = null,
        open var mediaUrlHttps: String? = null,
//        open var sizes: Sizes,
        open var sourceStatusId: Long? = null,
        open var type: String? = null,
//        open var videoInfo: VideoInfo,
        open var altText: String? = null,
        open var start: Int = 0,
        open var end: Int = 0
) : RealmObject()

open class HashtagEntity (
        open var text: String? = null,
        open var start: Int? = null,
        open var end: Int? = null
) : RealmObject()

open class TweetEntities(
        open var urls: RealmList<UrlEntity> = RealmList(),
        open var userMentions: RealmList<MentionEntity> = RealmList(),
        open var media: RealmList<MediaEntity> = RealmList(),
        open var hashtags: RealmList<HashtagEntity> = RealmList()
) : RealmObject()

//open class BoundingBox(
        //TODO save this stupid box
//)

//open class Place(
//      //open var attributes: Map<String, String>,
//      open var boundingBox: BoundingBox,
//      open var country: String,
//      open var countryCode: String,
//      open var fullName: String,
//      open var id: String,
//      open var name: String,
//      open var placeType: String,
//      open var url: String
//) : RealmObject()


//open class BindingValues(
//        type: String
//        //TODO save map to realm
//)
//open class Card(
//        open var bindingValues: BindingValues,
//        open var name: String
//) : RealmObject()
//
//open class RealmInt(
//        open var value: Int
//) : RealmObject()

open class RealmString(
        open var value: String? = null
) : RealmObject()

open class Tweet(
        @PrimaryKey open var id: Long = 0,
        open var idStr: String? = null,
        open var coordinates: Coordinates? = null,
        open var createdAt: String? = null,
        //open var currentUserRetweet: Object? = null,
        open var entities: TweetEntities? = null,
        open var extendedEntities: TweetEntities? = null,
        open var favoriteCount: Int? = null,
        open var favorited: Boolean? = null,
        open var filterLevel: String? = null,
        open var inReplyToScreenName: String? = null,
        open var inReplyToStatusId: Long? = null,
        open var inReplyToStatusIdStr: String? = null,
        open var inReplyToUserId: Long? = null,
        open var inReplyToUserIdStr: String? = null,
        open var lang: String? = null,
        //open var place: Place? = null,
        open var possiblySensitive: Boolean? = null,
        //open var scopes: Object? = null,
        open var quotedStatusId: Long? = null,
        open var quotedStatusIdStr: String? = null,
        open var quotedStatus: Tweet? = null,
        open var retweetCount: Int? = null,
        open var retweeted: Boolean? = null,
        open var retweetedStatus: Tweet? = null,
        open var source: String? = null,
        open var text: String? = null,
        open var start: Int? = null,
        open var end: Int? = null,
        open var truncated: Boolean? = null,
        open var user: User? = null,
        open var withheldCopyright: Boolean? = null,
        open var withheldInCountries: RealmList<RealmString> = RealmList(),
        open var withheldScope: String? = null
        //open var card: Card
) : RealmObject()


