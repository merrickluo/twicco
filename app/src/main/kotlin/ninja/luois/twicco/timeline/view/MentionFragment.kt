package ninja.luois.twicco.timeline.view

import android.os.Bundle
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.timeline.provider.TimelineProvider
import rx.Observable

class MentionFragment : TimelineFragment() {
    override val tweetLoader: (Long?, Long?) -> Observable<List<Tweet>>
        get() = { sinceId, maxId ->
            TimelineProvider.mentionTimeline_(sinceId, maxId)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity.title = "mention"
    }

}
