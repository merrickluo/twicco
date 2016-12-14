package ninja.luois.twicco.extension.ui

import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

fun Context.showShortToast(@StringRes res: Int) {
    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
}

fun Context.showShortToast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}
