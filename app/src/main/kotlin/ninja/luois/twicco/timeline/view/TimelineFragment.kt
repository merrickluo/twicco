package ninja.luois.twicco.timeline.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Fragment
import ninja.luois.twicco.timeline.provider.TimelineProvider
import ninja.luois.twicco.timeline.viewmodel.TweetAdapter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.filterNotNull
import kotlin.properties.Delegates

abstract class TimelineFragment : Fragment() {
    var listView : RecyclerView by Delegates.notNull()

    abstract val tweetsData: Observable<List<Tweet>>

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

        tweetsData.bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .filterNotNull()
                .subscribeTo {
                    next {
                        adapter.tweets = it
                        adapter.notifyDataSetChanged()
                    }
                    error {
                        Toast.makeText(activity, "error fetching tweets", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}

