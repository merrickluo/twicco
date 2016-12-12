package ninja.luois.twicco.compose.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jakewharton.rxbinding.view.clicks
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.compose.provider.NewTweetProvider
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.extension.observable.withDialog
import rx.android.schedulers.AndroidSchedulers

private fun String.isValidTweet(): Boolean {
    return isNotEmpty() && length < 140;
}

class NewTweetActivity : Activity() {

    val tweetEditText by bindView<EditText>(R.id.text_tweet)
    val tweetButton by bindView<Button>(R.id.button_tweet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_tweet)
        title = "new tweet"

        tweetButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    tweet(tweetEditText.text.toString())
                }
    }

    private fun tweet(text: String) {
        //TODO: validate on the fly
        if (text.isValidTweet()) {
            NewTweetProvider.tweet_(text)
                    .withDialog(this, "Sending")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeTo {
                        success = {
                            Toast.makeText(this@NewTweetActivity, "success", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        error = {
                            Toast.makeText(this@NewTweetActivity, "failed", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}
