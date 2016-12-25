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
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Fragment
import ninja.luois.twicco.compose.view.NewTweetActivity
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
    var listView : RecyclerView by Delegates.notNull()
    var refreshLayout: SwipeRefreshLayout by Delegates.notNull()

    var footerView: View by Delegates.notNull()
    var footerProgressBar: View by Delegates.notNull()

    val tweets_: Variable<List<Tweet>> = Variable(emptyList())
    val refreshing_: Variable<Boolean> = Variable(false)
    val loading_: Variable<Boolean> = Variable(false)
    val error_: PublishSubject<String> = PublishSubject.create()

    var adapter: TweetAdapter by Delegates.notNull()

    abstract val tweetLoader: (sinceId: Long?, maxId: Long?) -> Observable<List<Tweet>>

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (inflater == null) return null

        footerView = inflater.inflate(R.layout.item_timeline_footer, null, false)
        footerProgressBar = footerView.findViewById(R.id.footer_timeline_progress)

        val rootView = inflater.inflate(R.layout.fragment_timeline, container, false)
        listView = rootView.findViewById(R.id.list_timeline) as RecyclerView
        refreshLayout = rootView.findViewById(R.id.refresh_layout_timeline) as SwipeRefreshLayout

        return rootView
    }
    private val lm = LinearLayoutManager(activity)
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


        listView.layoutManager = lm
        // load more when about to scroll down to bottom
        listView.scrollEvents()
                .bindToLifecycle(this)
                .filter {
                    tweets_.value.isNotEmpty()  // do not not more when empty
                            && lm.findLastVisibleItemPosition() > tweets_.value.size - 5
                }
                .subscribe { loadMore() }

        adapter = TweetAdapter(activity, footerView) { t, action ->
            when (action) {
                Action.Retweet -> {
                    if (t.retweeted) {
                        activity.showShortToast("undo retweeting")
                        // TODO: need local cache to update favorite state
                        TimelineProvider.unretweet_(t.id)
                                .bindToLifecycle(this)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { adapter.update(it) }
                    } else {
                        // TODO: need local cache to update favorite state
                        activity.showShortToast("retweeting")
                        TimelineProvider.retweet_(t.id)
                                .bindToLifecycle(this)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { adapter.update(it) }
                    }
                }
                Action.Heart -> {
                    // TODO: undo heart
                    // TODO: need local cache to update favorite state
                    activity.showShortToast("favoriting")
                    TimelineProvider.heart_(t.id)
                            .bindToLifecycle(this)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { adapter.update(t) }
                }
                Action.Reply -> {
                    val prefix = "@${t.user.screenName} "
                    NewTweetActivity.start(activity, NewTweetActivity.Type.Reply, t.id, prefix)
                }
                Action.Quote -> {

                    NewTweetActivity.start(activity, NewTweetActivity.Type.Quote, t.id)
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

