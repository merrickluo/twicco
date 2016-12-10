package ninja.luois.twicco.timeline.view

import android.os.Bundle
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.timeline.provider.TimelineProvider
import rx.Observable

class MentionFragment : TimelineFragment() {

    override val tweetsData: Observable<List<Tweet>>
        get() = TimelineProvider.mentionTimeline_()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity.title = "mention"
    }

}
