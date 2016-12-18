package ninja.luois.twicco.common

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterCore
import io.fabric.sdk.android.Fabric
import ninja.luois.twicco.R

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = TwitterAuthConfig(
                getString(R.string.twitter_key),
                getString(R.string.twitter_secret))

        Fabric.with(this, TwitterCore(config))

        Fresco.initialize(this)
    }
}
