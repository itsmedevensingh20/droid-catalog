package com.droidinsight.catalog.ui.widgets

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.droidinsight.catalog.R
import com.droidinsight.catalog.base.BaseActivity
import com.droidinsight.catalog.databinding.ActivityWidgetsBinding
import com.droidinsight.catalog.databinding.LayoutToolbarBinding
import com.google.android.material.tabs.TabLayoutMediator
import doBack
import statusBarTransparent

class WidgetsActivity : BaseActivity<ActivityWidgetsBinding, WidgetsViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolBarBinding = DataBindingUtil.setContentView(
            this@WidgetsActivity,
            R.layout.layout_toolbar
        )
        statusBarTransparent()
        bindViewModel()
        init()
        initControl()
    }

    private lateinit var toolBarBinding: LayoutToolbarBinding
    override val layoutRes: Int
        get() = R.layout.activity_widgets
    override val viewModelClass: Class<WidgetsViewModel>
        get() = WidgetsViewModel::class.java

    override fun bindViewModel() {
        binding.widgetsViewModel = viewModel
        binding.lifecycleOwner = this

    }

    private val titles = arrayOf("Movies", "Events", "Tickets")
    override fun init() {
        toolBarBinding.backBtnToolbar.doBack()
        headingToolbar.text = getString(R.string.android_tutorial)


    }

    override fun initControl() {
        setSupportActionBar(toolBarWidgets)
        mAdapter()
    }

    private fun mAdapter() {
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = pagerAdapter.getTabTitle(position)
        }.attach()
    }
}