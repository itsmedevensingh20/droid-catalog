package com.droidinsight.catalog.extensionFunction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

fun Activity.goToNext(intent: Intent, rq: Int = -1) =
    if (rq == -1) startActivity(intent) else startActivityForResult(intent, rq)

fun Activity.gotoFinish(intent: Intent) {
    startActivity(intent)
    finish()
}
fun Activity.goto(intent: Intent, rq: Int = -1) =
    if (rq == -1) startActivity(intent) else startActivityForResult(intent, rq)


fun Activity.gotoNewTask(intent: Intent) {
    startActivity(intent.apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    })
    finish()
}

fun Activity.goToNext(cls: Class<*>, rq: Int = -1) =
    if (rq == -1) startActivity(Intent(this, cls)) else startActivityForResult(
        Intent(this, cls),
        rq
    )

fun Activity.gotoFinish(cls: Class<*>) {
    startActivity(Intent(this, cls))
    finish()
}

fun Activity.gotoNewTask(cls: Class<*>) {
    startActivity(Intent(this, cls).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    })
    finish()
}

fun Activity.hideKeyboard() {
    val view = currentFocus
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Activity.showKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

