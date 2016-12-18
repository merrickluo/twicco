package ninja.luois.twicco.timeline.viewmodel

import android.graphics.Color
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import com.twitter.sdk.android.core.models.Tweet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private class ClickStringSpan(val type: TweetLinkType,
                              val content: String,
                              val action: (TweetLinkType, String) -> Unit) : ClickableSpan() {
    override fun onClick(view: View?) {
        action(type, content)
    }

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.color = Color.WHITE
    }
}

private fun SpannableString.setLinkSpan(start: Int,
                                        end: Int,
                                        content: String,
                                        type: TweetLinkType,
                                        action: (TweetLinkType, String) -> Unit) {
    setSpan(ClickStringSpan(type, content, action), start, end,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

}

enum class TweetLinkType {
    HashTag,Media,Url,User
}

// dirty workaround with java utf-16 string and 8bit byte
fun Tweet.displayText(linkAction: (TweetLinkType, String) -> Unit): CharSequence {
    var showText = StringBuilder(text)
    // replace url with displayUrl
    entities.urls?.forEach {
        val start = showText.indexOf(it.url, ignoreCase = true)
        val end = start + it.url.toCharArray().size
        showText = StringBuilder(showText.replaceRange(start, end, it.displayUrl))
    }
    // remove media url
    entities.media?.forEach {
        val start = showText.indexOf(it.url, ignoreCase = true)
        val end = start + it.url.toCharArray().size
        showText = StringBuilder(showText.replaceRange(start, end, ""))
    }

    // highlight entities
    val sb = SpannableString(showText)
    entities.hashtags?.forEach {
        val start = sb.indexOf("#${it.text}", ignoreCase = true) // #hashtag
        val end = start + it.text.toCharArray().size + 1
        sb.setLinkSpan(start, end, it.text, TweetLinkType.HashTag, linkAction)
    }
    entities.userMentions?.forEach {
        val start = sb.indexOf("@${it.screenName}", ignoreCase = true) // @screenName
        val end = start + it.screenName.toCharArray().size + 1
        sb.setLinkSpan(start, end, it.screenName, TweetLinkType.User, linkAction)
    }

    entities.urls?.forEach {
        val start = sb.indexOf(it.displayUrl, ignoreCase = true)
        val end = start + it.displayUrl.toCharArray().size
        sb.setLinkSpan(start, end, it.expandedUrl, TweetLinkType.Url, linkAction)
    }

    return sb
}

fun Tweet.displayID(): String {
    return "@${user.screenName}"
}

data class Media(val type: Type, val url: String) {
    enum class Type {
        Gif, Video, Photo;

        companion object {
            fun of(value: String): Type? {
                Log.i("tweet", value)
                return when (value) {
                    "animated_gif" -> Gif
                    "video" -> Video
                    else -> Photo
                }
            }
        }

        fun displayName(): String {
            return when (this) {
                Gif -> "GIF"
                Video -> "VIDEO"
                else -> ""
            }
        }
    }
}

class TweetViewModel(val rawTweet: Tweet) {
    private val dateFormat: DateFormat
    private val showingTweet: Tweet

    var linkAction: ((TweetLinkType, String) -> Unit)? = null

    enum class Type(val value: Int) {
        Normal(0), Image(1), Quote(2)
    }

    init {
        dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ROOT)
        showingTweet = rawTweet.retweetedStatus ?: rawTweet
    }

    val avatarUrl: String
        get() {
            val small = showingTweet.user.profileImageUrlHttps
            return small.replace("_normal", "_bigger")
        }

    val id: String
        get() = showingTweet.displayID()

    val name: String
        get() = showingTweet.user.name

    val text: CharSequence
        get() = showingTweet.displayText() { type, text ->
            linkAction?.invoke(type, text)
        }

    val time: String
        get() {
            val tweetDate = dateFormat.parse(showingTweet.createdAt)
            val now = Date()
            var diff = (now.time - tweetDate.time) / 1000
            if (diff < 0) {
                diff = 0
            }

            return when {
                diff < 60 -> "${diff}s"
                diff < 60*60 -> "${diff/60}m"
                else -> "${diff/60/60}h"
            }
        }

    val medias: List<Media>
        get() {
            return rawTweet.extendedEtities?.media?.map {
                val type = Media.Type.of(it.type) ?: Media.Type.Photo
                Media(type, it.mediaUrl)
            }
            ?: emptyList()
        }

    val via: String
        get() {
            val regex = Regex("<a\\b[^>]*>(.*?)</a>")
            val result = regex.find(showingTweet.source)

            val text = "via ${result?.groupValues?.get(1) ?: ""}"
            if (!rawTweet.inReplyToScreenName.isNullOrBlank()) {
                return "$text in reply to @${rawTweet.inReplyToScreenName}"
            } else {
                return text
            }
        }

    val quoteTweet: Tweet
        get() = rawTweet.quotedStatus!!

    val hasRetweet: Boolean
        get() = rawTweet.retweetedStatus != null

    val retweet: String
        get() {
            return "Retweeted by ${rawTweet.user.name} and ${rawTweet.retweetCount} others"
        }

    val type: Type
        get() {
            if (rawTweet.quotedStatus != null) {
                return Type.Quote
            }
            if (rawTweet.entities?.media != null) {
                return Type.Image
            }
            return Type.Normal
        }
}































