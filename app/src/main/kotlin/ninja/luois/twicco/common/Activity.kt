package ninja.luois.twicco.common

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import ninja.luois.twicco.R

abstract class Activity: RxAppCompatActivity() {
    abstract var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base)
        supportFragmentManager.beginTransaction()
                .add(R.id.content, fragment)
                .commit()
    }

}