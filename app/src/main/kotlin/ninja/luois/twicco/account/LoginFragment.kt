package ninja.luois.twicco.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Fragment
import kotlin.properties.Delegates

class LoginFragment : Fragment() {
    var loginButton: TwitterLoginButton by Delegates.notNull()
    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_login, container, false)!!
        loginButton = root.findViewById(R.id.button_connect) as TwitterLoginButton
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginButton.callback = object : Callback<TwitterSession>() {
            override fun failure(exception: TwitterException?) {
                Log.e("LoginFragment", "Login error", exception)
            }

            override fun success(result: Result<TwitterSession>?) {
                Log.wtf("LoginFragment", "shit")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }
}