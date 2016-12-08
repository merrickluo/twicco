package ninja.luois.twicco.common

import android.app.Application
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterCore
import io.fabric.sdk.android.Fabric

class Application : Application() {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private val TWITTER_KEY = ""
    private val TWITTER_SECRET = ""

    override fun onCreate() {
        super.onCreate()

        val config = TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET)
        Fabric.with(this, TwitterCore(config))
    }
}
