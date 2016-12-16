package ninja.luois.twicco.timeline.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jakewharton.rxbinding.view.clicks
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.timeline.view.*


class TweetAdapter(val ctx: Context) : RecyclerView.Adapter<TweetViewHolder>() {
    var tweets: List<Tweet> = emptyList()

    val linkAction = { type: TweetLinkType, content: String ->
        when (type) {
            TweetLinkType.Url -> {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(content))
                ctx.startActivity(i)
            }
        }
    }

    private fun bindNormalViewHolder(holder: TweetViewHolder, vm: TweetViewModel) {
        Picasso.with(ctx).load(vm.avatarUrl).into(holder.avatarView)
        holder.nameView.text = vm.name
        holder.idView.text = vm.id
        holder.timeView.text = vm.time
        holder.tweetView.text = vm.text
        holder.viaView.text = vm.via

        if (vm.hasRetweet) {
            holder.retweetView.visibility = View.VISIBLE
            holder.retweetView.text = vm.retweet
        } else {
            holder.retweetView.visibility = View.GONE
        }

        vm.linkAction = linkAction
    }

    private fun bindQuoteViewHolder(holder: QuoteTweetViewHolder, vm: TweetViewModel) {
        if (vm.hasQuote) {
            holder.quoteIdView.text = vm.quoteId
            holder.quoteNameView.text = vm.quoteName
            holder.quoteTweetView.text = vm.quoteTweet
        }
    }

    private fun bindImageViewHolder(holder: ImageTweetViewHolder, vm: TweetViewModel) {
        for (i in 0..3) {
            if (i < vm.imageUrls.size) {
                holder.imageViewAt(i)?.visibility = View.VISIBLE
            } else {
                holder.imageViewAt(i)?.visibility = View.GONE
            }
        }

        vm.imageUrls.forEachIndexed { i, url ->
            holder.imageViewAt(i)?.let { imageView ->

                Picasso.with(ctx)
                        .load(url)
                        .into(imageView)

                imageView.clicks()
                        .subscribe {
                            val ms = vm.imageUrls.map(::Media)
                            val fm = (ctx as Activity).fragmentManager
                            MediaPreviewDialog(ms, i)
                                    .show(fm, "media preview")
                        }
            }
        }
    }

    override fun onBindViewHolder(holder: TweetViewHolder?, position: Int) {
        if (holder == null) return
        val vm = TweetViewModel(tweets[position])
        bindNormalViewHolder(holder, vm)

        when (getItemViewType(position)) {
            TweetViewModel.Type.Quote.value -> {
                bindQuoteViewHolder(holder as QuoteTweetViewHolder, vm)
            }
            TweetViewModel.Type.Image.value -> {
                bindImageViewHolder(holder as ImageTweetViewHolder, vm)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val vm = TweetViewModel(tweets[position])
        return vm.type.value
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TweetViewHolder {
        val inflater = LayoutInflater.from(ctx)
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return when (viewType) {
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
            else -> TweetViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

}






















