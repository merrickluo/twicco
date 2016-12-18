package ninja.luois.twicco.timeline.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jakewharton.rxbinding.view.clicks
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.timeline.view.*

enum class Action {
    Reply, Retweet, Heart, Quote, More
}

class TweetAdapter(val ctx: Context,
                   val footer: View,
                   val action: (Tweet, Action) -> Unit) : RecyclerView.Adapter<TweetViewHolder>() {

    var tweets: List<Tweet> = emptyList()

    private val viewTypeFooter = 999

    val linkAction = { type: TweetLinkType, content: String ->
        when (type) {
            TweetLinkType.Url -> {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(content))
                ctx.startActivity(i)
            }
        }
    }

    private fun bindNormalViewHolder(holder: TextTweetViewHolder, vm: TweetViewModel) {
        holder.avatarView.setImageURI(Uri.parse(vm.avatarUrl), ctx)
        holder.nameView.text = vm.name
        holder.idView.text = vm.id
        holder.timeView.text = vm.time
        holder.viaView.text = vm.via
        holder.tweetView.text = vm.text

        if (vm.text.isNullOrBlank()) {

        } else {
            holder.tweetView.visibility = View.VISIBLE
        }
        vm.linkAction = linkAction

        if (vm.hasRetweet) {
            holder.retweetView.visibility = View.VISIBLE
            holder.retweetView.text = vm.retweet
        } else {
            holder.retweetView.visibility = View.GONE
        }

        holder.replyButton.clicks()
                .subscribe { action(vm.rawTweet, Action.Reply) }
        holder.retweetButton.clicks()
                .subscribe { action(vm.rawTweet, Action.Retweet) }
        holder.heartButton.clicks()
                .subscribe { action(vm.rawTweet, Action.Heart) }
        holder.quoteButton.clicks()
                .subscribe { action(vm.rawTweet, Action.Quote) }

        // should toggle menu
        holder.moreButton.clicks()
                .subscribe { action(vm.rawTweet, Action.More) }

    }

    private fun bindQuoteViewHolder(holder: QuoteTweetViewHolder, vm: TweetViewModel) {
        holder.quoteIdView.text = vm.id
        holder.quoteNameView.text = vm.name
        holder.quoteTweetView.text = vm.text

        holder.quoteImageView.visibility = View.GONE
        vm.medias.firstOrNull()?.let {
            holder.quoteImageView.visibility = View.VISIBLE
            holder.quoteImageView.setImageURI(Uri.parse(it.url), ctx)
            holder.quoteImageView.clicks()
                    .subscribe {
                        val fm = (ctx as Activity).fragmentManager
                        MediaPreviewDialog(vm.medias)
                                .show(fm, "media preview")
                    }
        }
    }

    private fun bindImageViewHolder(holder: ImageTweetViewHolder, vm: TweetViewModel) {
        for (i in 0..3) {
            if (i < vm.medias.size) {
                holder.imageViewAt(i)?.visibility = View.VISIBLE
            } else {
                holder.imageViewAt(i)?.visibility = View.GONE
            }
        }

        vm.medias.forEachIndexed { i, media ->
            holder.mediaTypeTextView.text = media.type.displayName()
            Log.i("Tweet", media.type.displayName())

            holder.imageViewAt(i)?.let { imageView ->
                imageView.setImageURI(Uri.parse(media.url), ctx)

                imageView.clicks()
                        .subscribe {
                            val fm = (ctx as Activity).fragmentManager
                            MediaPreviewDialog(vm.medias, i)
                                    .show(fm, "media preview")
                        }
            }
        }
    }

    override fun onBindViewHolder(holder: TweetViewHolder?, position: Int) {
        if (holder == null) return
        if (position == tweets.size) return // footer view
        val vm = TweetViewModel(tweets[position])
        bindNormalViewHolder(holder as TextTweetViewHolder, vm)

        when (getItemViewType(position)) {
            TweetViewModel.Type.Quote.value -> {
                bindQuoteViewHolder(holder as QuoteTweetViewHolder, TweetViewModel(vm.quoteTweet))
            }
            TweetViewModel.Type.Image.value -> {
                bindImageViewHolder(holder as ImageTweetViewHolder, vm)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == tweets.size) {
            return viewTypeFooter
        }
        val vm = TweetViewModel(tweets[position])
        return vm.type.value
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TweetViewHolder {
        val inflater = LayoutInflater.from(ctx)
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return when (viewType) {
            viewTypeFooter -> FooterViewHolder(footer)

            TweetViewModel.Type.Image.value -> {
                val mediaView = inflater.inflate(R.layout.item_tweet_images, parent, false)
                val container = view.findViewById(R.id.layout_quote) as FrameLayout
                container.visibility = View.VISIBLE
                container.addView(mediaView)
                ImageTweetViewHolder(view)
            }
            TweetViewModel.Type.Quote.value -> {
                val quote = inflater.inflate(R.layout.item_quote_tweet, parent, false)
                val container = view.findViewById(R.id.layout_quote) as FrameLayout
                container.visibility = View.VISIBLE
                container.addView(quote)
                QuoteTweetViewHolder(view)
            }
            else -> TextTweetViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return tweets.size + 1
    }

    private class FooterViewHolder(view: View) : TweetViewHolder(view)
}






















