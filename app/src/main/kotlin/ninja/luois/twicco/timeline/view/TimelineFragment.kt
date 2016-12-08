package ninja.luois.twicco.timeline.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Fragment
import ninja.luois.twicco.timeline.provider.TimelineProvider
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import kotlin.properties.Delegates

class TimelineFragment : Fragment() {
    var listView : RecyclerView by Delegates.notNull()
    val provider = TimelineProvider()

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_timeline, container, false)!!
        listView = root.findViewById(R.id.list_timeline) as RecyclerView

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = TweetAdapter(activity)
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = adapter

        provider.homeTimeline_()
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.tweets = it
                    adapter.notifyDataSetChanged()
                }
    }
}

class TweetAdapter(val context: Context) : RecyclerView.Adapter<TweetViewHolder>() {
    var tweets: List<Tweet> = emptyList()

    override fun onBindViewHolder(holder: TweetViewHolder?, position: Int) {
        if (holder == null) return

        val t = tweets[position]

        Picasso.with(context).load(t.user.profileImageUrl).into(holder.avatarView)
        holder.nameView.text = t.user.name
        holder.idView.text = "@" + t.user.screenName

        val d = Date(t.createdAt)
        holder.timeView.text = DateUtils.getRelativeDateTimeString(
                context, d.time, Date().time, 0L, DateUtils.FORMAT_ABBREV_RELATIVE)

        holder.tweetView.text = t.text
        holder.viaView.text = "via twicco"
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

class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var colorLabel: View by Delegates.notNull()
    var avatarView: ImageView by Delegates.notNull()
    var nameView: TextView by Delegates.notNull()
    var idView: TextView by Delegates.notNull()
    var timeView: TextView by Delegates.notNull()
    var tweetView: TextView by Delegates.notNull()
    var viaView: TextView by Delegates.notNull()

    init {
        colorLabel = view.findViewById(R.id.view_color_label)
        avatarView = view.findViewById(R.id.image_avatar) as ImageView
        nameView = view.findViewById(R.id.text_name) as TextView
        idView = view.findViewById(R.id.text_id) as TextView
        timeView = view.findViewById(R.id.text_time) as TextView
        tweetView = view.findViewById(R.id.text_tweet) as TextView
        viaView = view.findViewById(R.id.text_via) as TextView
    }
}
