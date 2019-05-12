@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package hossein.gheisary.carshow.utility.extensions

import android.app.Dialog
import android.os.Build
import android.view.WindowManager
import hossein.gheisary.carshow.BuildConfig

fun Dialog.doKeepDialog() {
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(this.window.attributes)
    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
    this.window.attributes = lp
}


fun Boolean.toInt(): Int {
    return if(this){0}else {8}
}