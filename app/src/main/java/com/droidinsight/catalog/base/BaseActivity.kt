package com.droidinsight.catalog.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droidinsight.catalog.util.ProgressDialogUtils

abstract class BaseActivity<D : ViewDataBinding, V : ViewModel> : AppCompatActivity() {

    abstract val layoutRes: Int
    abstract val viewModelClass: Class<V>
    abstract fun bindViewModel()
    abstract fun init()
    abstract fun initControl()
    protected lateinit var binding: D
    lateinit var viewModel: V

//    private fun getProgressBar(): ProgressBar = checkNotNull(progressBar)

    fun showProgress() {
        currentFocus?.clearFocus()
        hideProgress()
        ProgressDialogUtils.getInstance().showProgress(this)
//        getProgressBar().doVisible()
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//        )
    }

    fun hideProgress() {
        currentFocus?.clearFocus()
        ProgressDialogUtils.getInstance().hideProgress()

//        getProgressBar().doGone()
//        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutRes)
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }

//    fun setUpToolbar(res: Int = 0, colorId: Int = R.color.colorWhite) {
//        iv_backBtn.setOnClickListener {
//            onBackPressed()
//        }
//    }
}