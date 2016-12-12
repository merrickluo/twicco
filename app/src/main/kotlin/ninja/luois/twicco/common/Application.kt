package ninja.luois.twicco.common

import android.app.Application
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterCore
import io.fabric.sdk.android.Fabric
import ninja.luois.twicco.R

class Application : Application() {
    private val TWITTER_KEY = getString(R.string.twitter_key)
    private val TWITTER_SECRET = getString(R.string.twitter_secret)

    override fun onCreate() {
        super.onCreate()

        val config = TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET)
        Fabric.with(this, TwitterCore(config))
    }
}
