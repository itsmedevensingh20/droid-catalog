package com.droidinsight.catalog.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.ProgressBar
import com.droidinsight.catalog.R

class ProgressDialogUtils {

    private var mDialog: Dialog? = null
    private var dialog: ProgressBar? = null

    companion object {
        var progressDialog: ProgressDialogUtils? = null
        fun getInstance(): ProgressDialogUtils {
            if (progressDialog == null) {
                progressDialog = ProgressDialogUtils()
            }
            return progressDialog as ProgressDialogUtils
        }
    }


    fun getInstance(): ProgressDialogUtils {
        if (progressDialog == null) {
            progressDialog = ProgressDialogUtils()
        }
        return progressDialog as ProgressDialogUtils
    }

    fun showProgress(context: Context) {
        try {
            if (mDialog == null) {
                mDialog = Dialog(context)
                mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mDialog?.setContentView(R.layout.custom_progress_dailog)
                mDialog?.setCancelable(false)
                mDialog?.show()
            } else {
                mDialog?.show()
            }
        } catch (e: Exception) {
            e.message?.let { Log.e("MyExceptionsProgress", it) }
        }
    }

    fun hideProgress() {
        if (mDialog != null) {
            mDialog?.dismiss()
            mDialog = null
        }
    }
}