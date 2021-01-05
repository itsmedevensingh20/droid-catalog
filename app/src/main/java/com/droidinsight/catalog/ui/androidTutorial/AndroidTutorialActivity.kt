package com.droidinsight.catalog.ui.androidTutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.droidinsight.catalog.R
import com.droidinsight.catalog.base.BaseActivity
import com.droidinsight.catalog.databinding.ActivityAndroidTutorialBinding
import doBack
import doGone
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.android.ext.android.bind
import statusBarTransparent

class AndroidTutorialActivity :
    BaseActivity<ActivityAndroidTutorialBinding, AndroidTutorialViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        bindViewModel()
        init()
        initControl()
    }

    override val layoutRes: Int
        get() = R.layout.activity_android_tutorial
    override val viewModelClass: Class<AndroidTutorialViewModel>
        get() = AndroidTutorialViewModel::class.java

    override fun bindViewModel() {
        binding.androidTutorialViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        backBtnToolbar.doBack()
        headingToolbar.text = getString(R.string.android_tutorial)
    }

    override fun initControl() {
        mSetData()
    }

    private fun mSetData() {

    }
}