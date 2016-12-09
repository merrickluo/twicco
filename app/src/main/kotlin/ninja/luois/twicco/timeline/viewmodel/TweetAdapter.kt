package ninja.luois.twicco.timeline.viewmodel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.R
import ninja.luois.twicco.timeline.view.TweetViewHolder

class TweetAdapter(val context: Context) : RecyclerView.Adapter<TweetViewHolder>() {
    var tweets: List<Tweet> = emptyList()

    override fun onBindViewHolder(holder: TweetViewHolder?, position: Int) {
        if (holder == null) return

        val vm = TweetViewModel(tweets[position])

        Picasso.with(context).load(vm.avatarUrl).into(holder.avatarView)
        holder.nameView.text = vm.name
        holder.idView.text = vm.id
        holder.timeView.text = vm.time
        holder.tweetView.text = vm.text
        holder.viaView.text = vm.via

        if (vm.hasQuote) {
            holder.quoteView.visibility = View.VISIBLE
            holder.quoteIdView.text = vm.quoteId
            holder.quoteNameView.text = vm.quoteName
            holder.quoteTweetView.text = vm.quoteTweet
        } else {
            holder.quoteView.visibility = View.GONE
        }

        if (vm.hasRetweet) {
            holder.retweetView.visibility = View.VISIBLE
            holder.retweetView.text = vm.retweet
        } else {
            holder.retweetView.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TweetViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return TweetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

}






















