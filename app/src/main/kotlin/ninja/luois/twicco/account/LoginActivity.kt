package ninja.luois.twicco.account

import android.content.Intent
import android.os.Bundle
import com.twitter.sdk.android.core.TwitterCore
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.common.Fragment
import ninja.luois.twicco.home.view.MainActivity

class LoginActivity : Activity() {
    var fragment: Fragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (TwitterCore.getInstance().sessionManager.activeSession != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (f in supportFragmentManager.fragments) {
            f.onActivityResult(requestCode, resultCode, data)
        }
    }
}
