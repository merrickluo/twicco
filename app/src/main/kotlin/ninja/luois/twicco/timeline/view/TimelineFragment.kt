package ninja.luois.twicco.timeline.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.support.v4.widget.refreshes
import com.jakewharton.rxbinding.support.v4.widget.refreshing
import com.jakewharton.rxbinding.support.v7.widget.scrollEvents
import com.jakewharton.rxbinding.view.visibility
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import com.twitter.sdk.android.core.models.Tweet
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Fragment
import ninja.luois.twicco.extension.observable.Variable
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.extension.ui.showShortToast
import ninja.luois.twicco.timeline.provider.TimelineProvider
import ninja.luois.twicco.timeline.viewmodel.Action
import ninja.luois.twicco.timeline.viewmodel.TweetAdapter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.filterNotNull
import rx.subjects.PublishSubject
import kotlin.properties.Delegates

abstract class TimelineFragment : Fragment() {
    val listView : RecyclerView by bindView(R.id.list_timeline)
    val refreshLayout: SwipeRefreshLayout by bindView(R.id.refresh_layout_timeline)

    var footerView: View by Delegates.notNull()
    var footerProgressBar: View by Delegates.notNull()

    val tweets_: Variable<List<Tweet>> = Variable(emptyList())
    val refreshing_: Variable<Boolean> = Variable(false)
    val loading_: Variable<Boolean> = Variable(false)
    val error_: PublishSubject<String> = PublishSubject.create()

    abstract val tweetLoader: (sinceId: Long?, maxId: Long?) -> Observable<List<Tweet>>

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (inflater == null) return null

        footerView = inflater.inflate(R.layout.item_timeline_footer, null, false)
        footerProgressBar = footerView.findViewById(R.id.footer_timeline_progress)

        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        error_.bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { activity.showShortToast(it) }

        refreshLayout.refreshes()
                .bindToLifecycle(this)
                .subscribe { refresh() }

        refreshing_.asObservable()
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(refreshLayout.refreshing())

        loading_.asObservable()
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(footerProgressBar.visibility())


        val lm = LinearLayoutManager(activity)
        listView.layoutManager = lm
        // load more when about to scroll down to bottom
        listView.scrollEvents()
                .bindToLifecycle(this)
                .filter {
                    tweets_.value.isNotEmpty()  // do not not more when empty
                            && lm.findLastVisibleItemPosition() > tweets_.value.size - 5
                }
                .subscribe { loadMore() }

        val adapter = TweetAdapter(activity, footerView) { t, action ->
            when (action) {
                Action.Retweet -> {
                    if (t.retweeted) {
                        activity.showShortToast("undo retweeting")
                        TimelineProvider.unretweet_(t.id).subscribe()
                    } else {
                        activity.showShortToast("retweeting")
                        TimelineProvider.retweet_(t.id).subscribe()
                    }
                }
                Action.Heart -> {
                    // TODO: undo heart
                    activity.showShortToast("favoriting")
                    TimelineProvider.heart_(t.id).subscribe()
                }
            }
        }
        listView.adapter = adapter

        tweets_.asObservable().bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .filterNotNull()
                .subscribeTo {
                    next {
                        adapter.tweets = it
                        adapter.notifyDataSetChanged()
                    }
                }

        refresh()
    }

    fun refresh() {
        val current = tweets_.value.firstOrNull()
        if (refreshing_.value == true) return
        tweetLoader(current?.id, null)
                .doOnSubscribe { refreshing_.value = true }
                .doAfterTerminate { refreshing_.value = false }
                .subscribeTo {
                    next { tweets_.value = it + tweets_.value }
                    error { error_.onNext("cannot refresh: ${it.message}") }
                }
    }

    private var noMore = false
    fun loadMore() {
        val last = tweets_.value.lastOrNull()
        if (noMore) return
        if (loading_.value == true) return
        tweetLoader(null, last?.id)
                .doOnSubscribe { loading_.value = true }
                .doAfterTerminate { loading_.value = false }
                .subscribeTo {
                    next {
                        // maxId will return as well
                        val new = it.drop(1)
                        if (new.isEmpty()) { noMore = true }
                        tweets_.value = tweets_.value + new
                    }
                    error { error_.onNext("cannot load more: ${it.message}") }
                }
    }

}

