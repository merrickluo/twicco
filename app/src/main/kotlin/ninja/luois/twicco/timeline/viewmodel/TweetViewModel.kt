package ninja.luois.twicco.timeline.viewmodel

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.twitter.sdk.android.core.models.Tweet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private fun SpannableString.setLinkSpan(start: Int, end: Int) {
    setSpan(ForegroundColorSpan(Color.parseColor("#4286f4")), start, end,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
}

fun Tweet.displayText(): CharSequence {
    val sb = SpannableString(text)
    //hashtag
    entities.hashtags?.forEach {
        sb.setLinkSpan(it.start, it.end)
    }
    entities.media?.forEach {
        sb.setLinkSpan(it.start, it.end)
    }
    entities.urls?.forEach {
        sb.setLinkSpan(it.start, it.end)
    }
    entities.userMentions?.forEach {
        sb.setLinkSpan(it.start, it.end)
    }

    var end = displayTextRange[1]

    // some unicode has 2 char and twitter count as 1
    if (end < sb.length) {
        if (Character.isLowSurrogate(sb[end])) {
            end += 1
        }
    }
    return sb.subSequence(0, end)
}


fun Tweet.displayID(): String {
    return "@${user.screenName}"
}

class TweetViewModel(val rawTweet: Tweet) {
    val dateFormat: DateFormat
    val showingTweet: Tweet

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
        get() = showingTweet.displayText()

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

    val imageUrls: List<String>
        get() {
            return rawTweet.extendedEtities.media.map { it.mediaUrlHttps }
        }

    val via: String
        get() {
            val regex = Regex("<a\\b[^>]*>(.*?)</a>")
            val result = regex.find(showingTweet.source)
            return "via ${result?.groupValues?.get(1) ?: ""}"
        }

    val hasQuote: Boolean
        get() = rawTweet.quotedStatus != null

    val quoteId: String
        get() = rawTweet.quotedStatus!!.displayID()

    val quoteName: String
        get() = rawTweet.quotedStatus!!.user.name

    val quoteTweet: CharSequence
        get() = rawTweet.quotedStatus!!.displayText()

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































