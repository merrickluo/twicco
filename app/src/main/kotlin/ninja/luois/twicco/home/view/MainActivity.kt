package ninja.luois.twicco.home.view

import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentTabHost
import android.widget.Button
import android.widget.ImageButton
import com.jakewharton.rxbinding.view.clicks
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.compose.view.NewTweetActivity
import ninja.luois.twicco.timeline.view.*

class MainActivity : Activity() {

    val tabHost by bindView<FragmentTabHost>(R.id.tab_host)
    val newTweetButton by bindView<ImageButton>(R.id.button_new_tweet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupTabHost()
        setupFab()
    }

    private fun setupFab() {
        newTweetButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    startActivity(Intent(this, NewTweetActivity::class.java))
                }
    }

    private fun setupTabHost() {
        tabHost.setup(this, supportFragmentManager, android.R.id.tabcontent)

        tabHost.addTab(
                tabHost.newTabSpec("home")
                        .setIndicator(newTabButton(R.drawable.ic_tabbar_home)),
                HomeFragment::class.java,
                null)

        tabHost.addTab(
                tabHost.newTabSpec("mention")
                        .setIndicator(newTabButton(R.drawable.ic_tabbar_mention)),
                MentionFragment::class.java,
                null)

        tabHost.addTab(
                tabHost.newTabSpec("message")
                        .setIndicator(newTabButton(R.drawable.ic_tabbar_message)),
                MessageFragment::class.java,
                null)

        tabHost.addTab(
                tabHost.newTabSpec("me")
                        .setIndicator(newTabButton(R.drawable.ic_tabbar_me)),
                UserFragment::class.java,
                null)
    }

    private fun newTabButton(@DrawableRes img: Int): ImageButton {
        val b = layoutInflater.inflate(R.layout.button_tab, null, false) as ImageButton
        b.setImageResource(img)
        return b
    }
}

