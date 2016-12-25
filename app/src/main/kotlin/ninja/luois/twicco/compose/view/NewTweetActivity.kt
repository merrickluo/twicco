package ninja.luois.twicco.compose.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.enabled
import com.jakewharton.rxbinding.widget.text
import com.jakewharton.rxbinding.widget.textChanges
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import com.twitter.sdk.android.core.models.Tweet
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.compose.provider.ComposingTweet
import ninja.luois.twicco.compose.provider.Media
import ninja.luois.twicco.compose.provider.NewTweetProvider
import ninja.luois.twicco.extension.observable.Variable
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.extension.observable.withDialog
import ninja.luois.twicco.extension.ui.showShortToast
import ninja.luois.twicco.timeline.provider.TimelineProvider
import ninja.luois.twicco.timeline.viewmodel.TweetViewModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.filterNotNull
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


private fun String.isValidTweet(): Boolean {
    return isNotEmpty() && length <= 140;
}

class NewTweetActivity : Activity() {

    enum class Type {
        New, Reply, Quote;

        fun displayName(): String {
            return when (this) {
                New -> "new tweet"
                Reply -> "reply to"
                Quote -> "quoting"
            }
        }
    }

    companion object {
        private val EXTRA_TWEET_TYPE = "extra_tweet_type"
        private val EXTRA_TWEET_ID = "extra_tweet_id"
        private val EXTRA_TWEET_TEXT = "extra_tweet_text"

        fun start(ctx: Context, type: Type, tweetId: Long? = null, text: String = "") {
            val i = Intent(ctx, NewTweetActivity::class.java)
            i.putExtra(EXTRA_TWEET_TYPE, type)
            i.putExtra(EXTRA_TWEET_ID, tweetId)
            i.putExtra(EXTRA_TWEET_TEXT, text)
            ctx.startActivity(i)
        }
    }

    //Constants
    val kRequestPermissionCamera = 101

    val kRequestCamera = 201
    val kRequestPhoto = 202

    val tweetEditText by bindView<EditText>(R.id.text_tweet)
    val tweetButton by bindView<Button>(R.id.button_tweet)
    val tweetCountText by bindView<TextView>(R.id.text_tweet_count)
    val imageButton by bindView<Button>(R.id.button_image)
    val quoteTweetView by bindView<FrameLayout>(R.id.reply_tweet_view)

    val tweet = ComposingTweet()

    var quoteTweet = Variable<Tweet?>(null)

    private val type: Type
        get() =  intent.getSerializableExtra(EXTRA_TWEET_TYPE) as Type

    private val quoteId: Long?
        get() = intent.getSerializableExtra(EXTRA_TWEET_ID) as Long?

    private val quoteText: String
        get() = intent.getSerializableExtra(EXTRA_TWEET_TEXT) as String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_tweet)

        refreshTitle()

        tweetButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    tweet()
                }

        quoteId?.let {
            TimelineProvider.tweet_(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .bindToLifecycle(this)
                    .subscribeTo {
                        next { quoteTweet.value = it }
                        error { /* ignore here */ }
                    }
        }

        quoteTweet.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .filterNotNull()
                .subscribe {
                    tweet.quote = "http://twitter.com/${it.user.screenName}/statuses/${it.idStr}"
                    refreshTitle()
                    refreshQuoteView(it)
                }

        // setup new tweet
        if (type == Type.Quote) {
            tweet.quote = "http://twitter.com/statuses/$quoteId"
        }
        tweet.replyTo = quoteId
                tweetEditText.setText(quoteText, TextView.BufferType.EDITABLE)
        tweetEditText.setSelection(quoteText.length)

        // check both text count and image count
        Observable.combineLatest(tweetEditText.textChanges(),
                imageButton.textChanges()) { text, imageCount ->
            text.toString().isValidTweet()
                    && (imageCount.isEmpty() || imageCount.toString().toInt() <= 4)
        }.subscribe(tweetButton.enabled())

        tweetEditText.textChanges()
                .bindToLifecycle(this)
                .subscribe { tweet.text = it.toString() }

        tweetEditText.textChanges()
                .bindToLifecycle(this)
                .doOnNext { tweetCountText.isEnabled = it.toString().length <= 140 }
                .map { (140 - it.length).toString() }
                .subscribe(tweetCountText.text())

        imageButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    showImageDialog()
                }
    }

    private fun upload(files: List<String>) {
        NewTweetProvider.upload_(files)
                .observeOn(AndroidSchedulers.mainThread())
                .withDialog(this, "uploading")
                .subscribeTo {
                    success = { ids ->
                        ids.forEachIndexed { i, id -> tweet.medias += Media(id, files[i]) }
                        setImageCount(tweet.medias.size)
                    }
                    error = { showShortToast("cannot upload image") }
                }
    }

    private fun refreshTitle() {
        val sb = StringBuilder(this.type.displayName())
        quoteTweet.value?.let {
            sb.append(" @")
            sb.append(it.user.screenName)
        }

        title = sb.toString()
    }

    private fun refreshQuoteView(tweet: Tweet) {
        quoteTweetView.visibility = View.VISIBLE
        val vm = TweetViewModel(tweet)
        val view = LayoutInflater.from(this)
                .inflate(R.layout.item_quote_tweet, quoteTweetView, false)
        val idView = view.findViewById(R.id.text_quote_id) as TextView
        val nameView = view.findViewById(R.id.text_quote_name) as TextView
        val tweetView = view.findViewById(R.id.text_quote_tweet) as TextView
        val imageView = view.findViewById(R.id.image_quote_tweet) as SimpleDraweeView

        idView.text = vm.id
        nameView.text = vm.name
        tweetView.text = vm.text

        imageView.visibility = View.GONE
        vm.medias.firstOrNull()?.let {
            imageView.visibility = View.VISIBLE
            imageView.setImageURI(Uri.parse(it.url), this)
        }

        quoteTweetView.addView(view)
    }

    private fun clearMediaAt(position: Int) {
        Log.e("Tweet", "remove image at $position")
        tweet.medias = tweet.medias.filterIndexed { i, media -> i != position }
        setImageCount(tweet.medias.size)
    }

    private fun setImageCount(count: Int) {
        when {
            count == 0 -> imageButton.text = ""
            count < 4 -> imageButton.text = "$count"
            else -> {
                val sb = SpannableString(tweet.medias.size.toString())
                sb.setSpan(ForegroundColorSpan(Color.RED),
                        0, sb.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                imageButton.text = sb
            }
        }
    }

    private fun showImageDialog() {
        ImagePreviewDialog(tweet.medias,
                { position -> clearMediaAt(position) },
                { startTakingPhoto() },
                { startPickingImage() })
                .show(fragmentManager, "image preview")
    }

    private fun startPickingImage() {
        //if (!ensurePermission(Manifest.permission.CAMERA, kRequestPermissionCamera)) return
        val pickIntent = Intent()
        pickIntent.type = "image/*"
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickIntent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(pickIntent,
                "select up to 4 photos"), kRequestPhoto)
    }

    private fun startTakingPhoto() {
        if (!ensurePermission(Manifest.permission.CAMERA, kRequestPermissionCamera)) return

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile = createImageFile()
            val uri = FileProvider.getUriForFile(this,"ninja.luois.twicco.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(takePictureIntent, kRequestCamera)
        }
    }

    fun ensurePermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            return false
        }
        return true
    }

    private fun tweet() {
        NewTweetProvider.tweet_(tweet)
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .withDialog(this, "Sending")
                .subscribeTo {
                    success = {
                        showShortToast("success")
                        finish()
                    }
                    error = {
                        showShortToast("failed")
                    }
                }
    }


    var currentTakingPhoto: String? = null
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentTakingPhoto = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return

        when (requestCode) {
            kRequestCamera -> {
                currentTakingPhoto?.let {
                    val f = File(it)
                    if (f.exists() && f.length() > 3*1024*1024) {
                    }
                    upload(listOf(it))
                }
            }
            kRequestPhoto -> {
                if (data?.clipData != null) {
                    // multiple select
                    val r = data?.clipData!!

                    var files: List<String> = emptyList()
                    for (i in 0..r.itemCount-1) {
                        val f = createImageFile()
                        r.getItemAt(i).uri?.let { uri ->
                            contentResolver.openInputStream(uri).use { ins ->
                                FileOutputStream(f).use { os ->
                                    ins.copyTo(os)
                                }
                            }
                            files += f.absolutePath
                        }
                    }

                    upload(files)
                } else {
                    //single select
                    data?.data?.let {
                        val f = createImageFile()
                        contentResolver.openInputStream(it).use { ins ->
                            FileOutputStream(f).use { os ->
                                ins.copyTo(os)
                            }
                        }
                        upload(listOf(f.absolutePath))
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            kRequestPermissionCamera -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTakingPhoto()
                } else {
                    showShortToast("cannot take photo without permission.")
                }
            }
        }
    }


}














