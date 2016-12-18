package ninja.luois.twicco.timeline.view

import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import kotterknife.bindView
import ninja.luois.twicco.R

abstract class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view)

class ImageTweetViewHolder(view: View) : TextTweetViewHolder(view) {
    private val image1View: SimpleDraweeView by bindView(R.id.image1)
    private val image2View: SimpleDraweeView by bindView(R.id.image2)
    private val image3View: SimpleDraweeView by bindView(R.id.image3)
    private val image4View: SimpleDraweeView by bindView(R.id.image4)

    val mediaTypeTextView: TextView by bindView(R.id.text_media_type)

    fun imageViewAt(position: Int): SimpleDraweeView? {
        return when (position) {
            0 -> image1View
            1 -> image2View
            2 -> image3View
            3 -> image4View
            else -> null
        }
    }
}

class QuoteTweetViewHolder(view: View) : TextTweetViewHolder(view) {
    val quoteNameView: TextView by bindView(R.id.text_quote_name)
    val quoteIdView: TextView by bindView(R.id.text_quote_id)
    val quoteTimeView: TextView by bindView(R.id.text_quote_time)
    val quoteTweetView: TextView by bindView(R.id.text_quote_tweet)
    val quoteImageView: SimpleDraweeView by bindView(R.id.image_quote_tweet)

    init {
        val m = quoteTweetView.movementMethod
        if (m == null || m !is LinkMovementMethod) {
            if (quoteTweetView.linksClickable) {
                quoteTweetView.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}

open class TextTweetViewHolder(view: View) : TweetViewHolder(view) {
    val colorLabel: View by bindView(R.id.view_color_label)
    val avatarView: SimpleDraweeView by bindView(R.id.image_avatar)
    val nameView: TextView by bindView(R.id.text_name)
    val idView: TextView by bindView(R.id.text_id)
    val timeView: TextView by bindView(R.id.text_time)
    val tweetView: TextView by bindView(R.id.text_tweet)
    val viaView: TextView by  bindView(R.id.text_via)
    val retweetView: TextView by bindView(R.id.text_retweet)

    val actionsLayout: View by bindView(R.id.layout_actions)
    val replyButton: Button by bindView(R.id.action_reply)
    val retweetButton: Button by bindView(R.id.action_retweet)
    val heartButton: Button by bindView(R.id.action_heart)
    val quoteButton: Button by bindView(R.id.action_quote)
    val moreButton: Button by bindView(R.id.action_more)

    init {
        val m = tweetView.movementMethod
        if (m == null || m !is LinkMovementMethod) {
            if (tweetView.linksClickable) {
                tweetView.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}























