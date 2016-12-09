package ninja.luois.twicco.timeline.viewmodel

import com.twitter.sdk.android.core.models.Tweet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Tweet.displayText(): String {
    val start = displayTextRange[0]
    var end = displayTextRange[1]

    // some unicode has 2 char and twitter count as 1
    if (end < text.length) {
        if (Character.isLowSurrogate(text[end])) {
            end += 1
        }
    }
    return text.substring(start, end)
}


fun Tweet.displayID(): String {
    return "@${user.screenName}"
}

class TweetViewModel(val rawTweet: Tweet) {
    val dateFormat: DateFormat
    val showingTweet: Tweet

    init {
        dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ROOT)
        showingTweet = rawTweet.retweetedStatus ?: rawTweet
    }

    val avatarUrl: String
        get() = showingTweet.user.profileImageUrlHttps

    val id: String
        get() = showingTweet.displayID()

    val name: String
        get() = showingTweet.user.name

    val text: String
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

    val quoteTweet: String
        get() = rawTweet.quotedStatus!!.displayText()

    val hasRetweet: Boolean
        get() = rawTweet.retweetedStatus != null

    val retweet: String
        get() {
            return "Retweeted by ${rawTweet.user.name} and ${rawTweet.retweetCount} others"
        }
}































