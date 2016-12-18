package ninja.luois.twicco.timeline.view

import android.os.Bundle
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.extension.observable.Variable
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.timeline.provider.TimelineProvider
import rx.Observable

class HomeFragment : TimelineFragment() {
    override val tweetLoader: (Long?, Long?) -> Observable<List<Tweet>>
        get() = { sinceId, maxId ->
            TimelineProvider.homeTimeline_(sinceId, maxId)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity.title = "home"
    }
}
