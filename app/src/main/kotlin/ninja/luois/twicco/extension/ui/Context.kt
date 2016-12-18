package ninja.luois.twicco.extension.ui

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.content.Context.WINDOW_SERVICE
import android.view.Display



fun Context.getScreenSize(): Point {
    val result = Point()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getSize(result)
    return result
}
