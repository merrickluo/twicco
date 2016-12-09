package ninja.luois.twicco.timeline.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Fragment
import ninja.luois.twicco.timeline.provider.TimelineProvider
import ninja.luois.twicco.timeline.viewmodel.TweetAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.filterNotNull
import kotlin.properties.Delegates

class TimelineFragment : Fragment() {
    var listView : RecyclerView by Delegates.notNull()
    val provider = TimelineProvider()

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_timeline, container, false)!!
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
                .filterNotNull()
                .subscribe {
                    adapter.tweets = it
                    adapter.notifyDataSetChanged()
                }
    }
}

