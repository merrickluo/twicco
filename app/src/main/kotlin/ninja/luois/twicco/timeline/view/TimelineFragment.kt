package ninja.luois.twicco.timeline.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding.support.v4.widget.refreshes
import com.jakewharton.rxbinding.support.v4.widget.refreshing
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import com.twitter.sdk.android.core.models.Tweet
import kotterknife.bindView
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Fragment
import ninja.luois.twicco.extension.observable.Variable
import ninja.luois.twicco.extension.ui.showShortToast
import ninja.luois.twicco.timeline.provider.TimelineProvider
import ninja.luois.twicco.timeline.viewmodel.TweetAdapter
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.filterNotNull
import rx.subjects.PublishSubject
import kotlin.properties.Delegates

abstract class TimelineFragment : Fragment() {
    val listView : RecyclerView by bindView(R.id.list_timeline)
    val refreshLayout: SwipeRefreshLayout by bindView(R.id.refresh_layout_timeline)

    val tweets_: Variable<List<Tweet>> = Variable(emptyList())
    val refreshing_: Variable<Boolean> = Variable(false)
    val loading_: Variable<Boolean> = Variable(false)
    val error_: PublishSubject<String> = PublishSubject.create()

    abstract val tweetLoader: (sinceId: Long?, maxId: Long?) -> Observable<List<Tweet>>

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_timeline, container, false)!!
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

        val adapter = TweetAdapter(activity)
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = adapter

        tweets_.asObservable().bindToLifecycle(this)
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

        refresh()
    }


    fun refresh() {
        val current = tweets_.value.firstOrNull()
        tweetLoader(current?.id, null)
                .doOnSubscribe { refreshing_.value = true }
                .doAfterTerminate { refreshing_.value = false }
                .subscribeTo {
                    next {
                        tweets_.value = it + tweets_.value
                    }
                    error {
                        error_.onNext("cannot refresh: ${it.message}")
                    }
                }
    }

    fun loadMore() {
        val last = tweets_.value.lastOrNull()
        tweetLoader(null, last?.id)
                .doOnSubscribe { loading_.value = true }
                .doAfterTerminate { loading_.value = false }
                .subscribeTo {
                    next {
                        tweets_.value = tweets_.value + it
                    }
                    error {
                        error_.onNext("cannot refresh: ${it.message}")
                    }
                }
    }

}

