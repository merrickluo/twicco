package ninja.luois.twicco.common

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import ninja.luois.twicco.R


abstract class Activity: RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle("twicco: " + title)
    }

    override fun setContentView(layoutResID: Int) {
        val fullView = layoutInflater.inflate(R.layout.activity_base, null) as RelativeLayout
        val activityContainer = fullView.findViewById(R.id.content) as FrameLayout
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(fullView)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        title = "Activity Title"
    }
}