package ninja.luois.twicco.extension.observable

import android.app.ProgressDialog
import android.content.Context
import rx.Observable
import rx.Single

fun <T> Observable<T>.withDialog(ctx: Context,
                                 message: String): Observable<T> {
    val dialog = ProgressDialog(ctx)
    dialog.setMessage(message)

    return this.doOnSubscribe {
        dialog.show()
    }.doAfterTerminate {
        dialog.dismiss()
    }
}
fun <T> Single<T>.withDialog(ctx: Context,
                             message: String): Single<T> {
    val dialog = ProgressDialog(ctx)
    dialog.setMessage(message)

    return this.doOnSubscribe {
        dialog.show()
    }.doAfterTerminate {
        dialog.dismiss()
    }
}
