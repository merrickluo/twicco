package ninja.luois.twicco.compose.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.enabled
import com.jakewharton.rxbinding.widget.text
import com.jakewharton.rxbinding.widget.textChanges
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.compose.provider.ComposingTweet
import ninja.luois.twicco.compose.provider.Media
import ninja.luois.twicco.compose.provider.NewTweetProvider
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.extension.observable.withDialog
import ninja.luois.twicco.extension.ui.showShortToast
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


private fun String.isValidTweet(): Boolean {
    return isNotEmpty() && length <= 140;
}

class NewTweetActivity : Activity() {

    //Constants
    val kRequestPermissionCamera = 101

    val kRequestCamera = 201
    val kRequestPhoto = 202

    val tweetEditText by bindView<EditText>(R.id.text_tweet)
    val tweetButton by bindView<Button>(R.id.button_tweet)
    val tweetCountText by bindView<TextView>(R.id.text_tweet_count)
    val imageButton by bindView<Button>(R.id.button_image)

    val tweet = ComposingTweet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_tweet)
        title = "new tweet"

        tweetButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    tweet()
                }

        // check both text count and image count
        Observable.combineLatest(tweetEditText.textChanges(),
                imageButton.textChanges()) { text, imageCount ->
            text.toString().isValidTweet()
                    && (imageCount.isEmpty() || imageCount.toString().toInt() <= 4)
        }.subscribe(tweetButton.enabled())

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














