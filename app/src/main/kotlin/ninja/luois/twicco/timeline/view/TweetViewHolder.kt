package ninja.luois.twicco.timeline.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotterknife.bindView
import ninja.luois.twicco.R


class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val colorLabel: View by bindView(R.id.view_color_label)
    val avatarView: ImageView by bindView(R.id.image_avatar)
    val nameView: TextView by bindView(R.id.text_name)
    val idView: TextView by bindView(R.id.text_id)
    val timeView: TextView by bindView(R.id.text_time)
    val tweetView: TextView by bindView(R.id.text_tweet)
    val viaView: TextView by  bindView(R.id.text_via)

    val quoteView: View by bindView(R.id.layout_quote)
    val quoteNameView: TextView by bindView(R.id.text_quote_name)
    val quoteIdView: TextView by bindView(R.id.text_quote_id)
    val quoteTimeView: TextView by bindView(R.id.text_quote_time)
    val quoteTweetView: TextView by bindView(R.id.text_quote_tweet)

    val retweetView: TextView by bindView(R.id.text_retweet)
}