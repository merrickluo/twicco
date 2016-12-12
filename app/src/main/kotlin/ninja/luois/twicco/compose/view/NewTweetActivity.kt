package ninja.luois.twicco.compose.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.enabled
import com.jakewharton.rxbinding.widget.text
import com.jakewharton.rxbinding.widget.textChanges
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.Activity
import ninja.luois.twicco.compose.provider.ComposingTweet
import ninja.luois.twicco.compose.provider.NewTweetProvider
import ninja.luois.twicco.extension.observable.subscribeTo
import ninja.luois.twicco.extension.observable.withDialog
import rx.android.schedulers.AndroidSchedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


private fun String.isValidTweet(): Boolean {
    return isNotEmpty() && length < 140;
}

class NewTweetActivity : Activity() {

    //Constants
    val kRequestPermissionCamera = 101
    val kRequestPermissionPhoto = 102

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

        tweetEditText.textChanges()
                .bindToLifecycle(this)
                // save to composing tweet
                .doOnNext { tweet.text = it.toString() }
                .map { it.toString().isValidTweet() }
                .subscribe(tweetButton.enabled())

        tweetEditText.textChanges()
                .bindToLifecycle(this)
                .map { (140 - it.length).toString() }
                .subscribe(tweetCountText.text())

        imageButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    showImageDialog()
                }
    }

    private fun showImageDialog() {
        AlertDialog.Builder(this)
                .setTitle("add an image")
                .setItems(arrayOf("take photo", "pick from gallery")) { dialog, index ->
                    when (index) {
                        0 -> startTakingPhoto()
                        1 -> startPickingImage()
                    }
                }
                .show()
    }

    private fun startPickingImage() {
        if (!ensurePermission(Manifest.permission.CAMERA, kRequestPermissionCamera)) return
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

    private fun upload(filepath: String) {
        NewTweetProvider.upload_(filepath)
                .bindToLifecycle(this)
                .withDialog(this, "uploading")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeTo {
                    success = {
                        tweet.mediaIds += it
                        imageButton.text = tweet.mediaIds.size.toString()
                    }
                    error = {
                        Toast.makeText(this@NewTweetActivity, "cannot upload image", Toast.LENGTH_SHORT).show()
                    }
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
                    upload(it)
                }
            }
            kRequestPhoto -> {
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
                    Toast.makeText(this@NewTweetActivity,
                            "cannot take photo without permission.",Toast.LENGTH_SHORT)
                            .show()
                }
            }
            kRequestPermissionPhoto -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPickingImage()
                } else {
                    Toast.makeText(this@NewTweetActivity,
                            "cannot pick image without permission.",Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }


}














