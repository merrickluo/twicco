package ninja.luois.twicco.compose.provider

import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.services.StatusesService
import ninja.luois.twicco.extension.observable.bgSingle
import rx.Single

object NewTweetProvider {

    val service: StatusesService

    init {
        service = TwitterCore.getInstance().apiClient.statusesService
    }

    fun tweet_(text: String): Single<Unit> {
        return bgSingle { s ->
            try {
                val resp = service.update(text, null, null, null, null, null, null, null, null)
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
}