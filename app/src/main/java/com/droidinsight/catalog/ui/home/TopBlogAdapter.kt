package com.droidinsight.catalog.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.droidinsight.catalog.R
import com.droidinsight.catalog.activity.WebViewActivity
import com.droidinsight.catalog.constant.Constants
import com.droidinsight.catalog.firebase.beam.TopPostBeam
import com.first_love.constant.EXTRA_DATA
import com.first_love.constant.EXTRA_FLAG
import kotlinx.android.synthetic.main.layout_top_post_viewpager.view.*
import setDebounceClickListener

class TopBlogAdapter : RecyclerView.Adapter<TopBlogAdapter.SearchUserViewHolder>() {

    private var mResponse: ArrayList<TopPostBeam> = ArrayList()
    private lateinit var activity: Activity

    inner class SearchUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val view = itemView
            Glide.with(view.context)
                .load(mResponse[position].top_post_images)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.bg_gradient_light_green)
                )
                .transform(FitCenter(), RoundedCorners(10))
                .into(view.userImages)

            view.setDebounceClickListener({
                activity.startActivityForResult(
                    Intent(view.context, WebViewActivity::class.java).apply {
                        putExtra(EXTRA_DATA, mResponse[position].top_post_link)
                        putExtra(EXTRA_FLAG, view.context.getString(R.string.app_name))
                    },
                    Constants.WEB_VIEWS
                )
            })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        return SearchUserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_top_post_viewpager,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int {
        return mResponse.size
    }

    fun setData(
        backGround: ArrayList<TopPostBeam>,
        activity: Activity

    ) {
        this.mResponse = backGround
        this.activity = activity
        notifyDataSetChanged()
    }
}