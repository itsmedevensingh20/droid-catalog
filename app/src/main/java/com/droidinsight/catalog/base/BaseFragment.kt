package com.droidinsight.catalog.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<D : ViewDataBinding> : Fragment() {

    abstract val layoutRes: Int
    abstract fun bindViewModel()
    abstract fun init()
    abstract fun initControl()
    protected lateinit var binding: D


//    private fun getProgressBar(): ProgressBar = checkNotNull(progressBar)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutRes, container, false) as D
        return binding.root
    }

//    fun setUpToolbar(res: Int = 0, colorId: Int = R.color.colorWhite) {
//        iv_backBtn.setOnClickListener {
//            activity?.onBackPressed()
//        }
//    }
}