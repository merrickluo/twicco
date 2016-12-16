package ninja.luois.twicco.timeline.view

import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotterknife.bindView
import ninja.luois.twicco.R

class ImageTweetViewHolder(view: View) : TweetViewHolder(view) {
    private val image1View: ImageView by bindView(R.id.image1)
    private val image2View: ImageView by bindView(R.id.image2)
    private val image3View: ImageView by bindView(R.id.image3)
    private val image4View: ImageView by bindView(R.id.image4)

    fun imageViewAt(position: Int): ImageView? {
        return when (position) {
            0 -> image1View
            1 -> image2View
            2 -> image3View
            3 -> image4View
            else -> null
        }
    }
}

class QuoteTweetViewHolder(view: View) : TweetViewHolder(view) {
    val quoteNameView: TextView by bindView(R.id.text_quote_name)
    val quoteIdView: TextView by bindView(R.id.text_quote_id)
    val quoteTimeView: TextView by bindView(R.id.text_quote_time)
    val quoteTweetView: TextView by bindView(R.id.text_quote_tweet)

    init {
        val m = quoteTweetView.movementMethod
        if (m == null || m !is LinkMovementMethod) {
            if (quoteTweetView.linksClickable) {
                quoteTweetView.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}

open class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val colorLabel: View by bindView(R.id.view_color_label)
    val avatarView: ImageView by bindView(R.id.image_avatar)
    val nameView: TextView by bindView(R.id.text_name)
    val idView: TextView by bindView(R.id.text_id)
    val timeView: TextView by bindView(R.id.text_time)
    val tweetView: TextView by bindView(R.id.text_tweet)
    val viaView: TextView by  bindView(R.id.text_via)
    val retweetView: TextView by bindView(R.id.text_retweet)

    init {
        val m = tweetView.movementMethod
        if (m == null || m !is LinkMovementMethod) {
            if (tweetView.linksClickable) {
                tweetView.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}