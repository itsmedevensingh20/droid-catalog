package com.droidinsight.catalog.util

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.droidinsight.catalog.R
import com.google.android.material.snackbar.Snackbar


object SnackbarUtils {
    @Suppress("unused")
    val TAG = SnackbarUtils::class.simpleName
    private fun displaySnackbar(
        view: View?,
        message: String?,
        duration: Int = Snackbar.LENGTH_SHORT
    ) {
        if (view == null) return
        if (message == null) {
            somethingWentWrong(view)
            return
        }

        val snackbar = Snackbar.make(view, message, duration)
        val tv =
            snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        tv.setTextColor(Color.WHITE)
        snackbar.show()
    }

    private fun somethingWentWrong(view: View?) {
        if (view == null) return
        displaySnackbar(
            view,
            view.context.getString(R.string.something_went_wrong)
        )
    }

    fun displayError(view: View?) {
        if (view == null) return
        displaySnackbar(
            view,
            view.context.getString(R.string.error_internet)
        )
    }

    /* fun displayError(view: View?, connectionException: ConnectException) {
         if (view == null) return
         displaySnackbar(
             view,
             view.context.com.blerdy.extension.getString(R.string.error_connection_please_check_internet)
         )
     }*/

//    fun displayError(view: View?, exception: HttpException) {
//        Log.i(TAG, "displayError()")
//        try {
////            val errorBody: ErrorBean = gsonInstance().fromJson(exception.response()
////                    .errorBody()?.string(), ErrorBean::class.java)
//            GsonUtil.mGsonInstance = Gson()
//            val errorBody = GsonUtil.mGsonInstance!!.fromJson(
//                exception.response()?.errorBody()?.string(), ResponseHandler.ErrorBean::class.java
//            )
//
//            displaySnackbar(view, errorBody.message)
//
//        } catch (e: Exception) {
//            somethingWentWrong(view)
//        }
//    }
}