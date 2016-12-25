package ninja.luois.twicco.compose.provider

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import ninja.luois.twicco.extension.observable.bgSingle
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Single
import java.io.ByteArrayOutputStream
import java.io.File


class ComposingTweet {
    var text: String? = null
    var replyTo: Long? = null
    var quote: String = ""
    var medias: List<Media> = emptyList()
}
data class Media(val id: String, val filepath: String)

object NewTweetProvider {

    val client: TwitterApiClient

    init {
        client = TwitterCore.getInstance().apiClient
    }

    fun tweet_(tweet: ComposingTweet): Single<Unit> {
        return bgSingle { s ->
            try {
                val mediaIds = tweet.medias.map { it.id }.joinToString(",")
                val text = "${tweet.text} ${tweet.quote}"

                val resp = client.statusesService.update(text,
                        tweet.replyTo, null, null, null, null, null, null, mediaIds)
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

    fun upload_(files: List<String>): Single<List<String>> {
        return bgSingle { s ->
            try {
                var ids: List<String> = emptyList()
                files.forEach {
                    val f = File(it)
                    Log.e("Tweet", "image size ${f.length()}")
                    val body = if (f.length() > 3*1024*1024) {
                        val image = resizedImageData(it)
                        RequestBody.create(MediaType.parse("image/webp"), image)
                    } else {
                        RequestBody.create(MediaType.parse("image/png"), f)
                    }

                    val resp = client.mediaService.upload(body, null, null)
                                .execute()
                    if (resp.isSuccessful) {
                        ids += resp.body().mediaIdString
                    } else {
                        s.onError(Exception(resp.errorBody().string()))
                    }
                }
                s.onSuccess(ids)
            } catch (tr: Throwable) {
                s.onError(tr)
            }
        }
    }

    fun resizedImageData(filepath: String): ByteArray {
        val b = BitmapFactory.decodeFile(filepath)
        val out = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.WEBP, 50, out)
        return out.toByteArray()
    }
}












