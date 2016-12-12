package ninja.luois.twicco.compose.provider

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterCore
import ninja.luois.twicco.extension.observable.bgSingle
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Single
import java.io.ByteArrayOutputStream
import java.io.File


class ComposingTweet {
    var text: String? = null
    var mediaIds: List<String> = emptyList()
}

object NewTweetProvider {

    val client: TwitterApiClient

    init {
        client = TwitterCore.getInstance().apiClient
    }


    fun tweet_(tweet: ComposingTweet): Single<Unit> {
        return bgSingle { s ->
            try {
                val mediaIds = tweet.mediaIds.joinToString(",")

                val resp = client.statusesService.update(tweet.text,
                        null, null, null, null, null, null, null, mediaIds)
                        .execute()

                if (resp.isSuccessful) {
                    s.onSuccess(Unit)
                } else {
                    s.onError(Exception(resp.errorBody().string()))
                }
            } catch (tr: Throwable) {
                s.onError(tr)
            }
        }
    }

    fun upload_(filepath: String): Single<String> {
        return bgSingle { s ->
            try {
                val image = resizeImage(filepath)
                Log.e("Tweet", "image size ${image.size}")
                val body = RequestBody.create(MediaType.parse("image/webp"), image)
                val resp = client.mediaService.upload(body, null, null)
                        .execute()

                if (resp.isSuccessful) {
                    s.onSuccess(resp.body().mediaIdString)
                } else {
                    s.onError(Exception(resp.errorBody().string()))
                }
            } catch (tr: Throwable) {
                s.onError(tr)
            }
        }
    }

    fun resizeImage(filepath: String): ByteArray {
        val b = BitmapFactory.decodeFile(filepath)
        val out = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.WEBP, 50, out)
        return out.toByteArray()
    }
}












