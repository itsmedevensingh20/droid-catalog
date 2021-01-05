package com.droidinsight.catalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droidinsight.catalog.R
import kotlinx.android.synthetic.main.layout_item_interest.view.*
import kotlin.collections.ArrayList

class InterestAdapter internal constructor(
    private val value: Int,
    private val interestList: ArrayList<String>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InterestHeadingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_item_interest,
                parent,
                false
            )
        )
    }
//        when (viewType) {
//            Constants.INTEREST_HEADING -> {
//                return InterestHeadingViewHolder(
//                    LayoutInflater.from(parent.context).inflate(
//                        R.layout.layout_item_interest_heading,
//                        parent,
//                        false
//                    )
//                )
//            }
//            Constants.INTEREST_TEXT -> {
//                return InterestViewHolder(
//                    LayoutInflater.from(parent.context).inflate(
//                        R.layout.layout_item_interest,
//                        parent,
//                        false
//                    )
//                )
//            }
//        }


//    override fun getItemViewType(position: Int): Int {
//        when (value) {
//            1 -> {
//                Constants.INTEREST_HEADING
//            }
//            2 -> {
//                Constants.INTEREST_TEXT
//            }
//        }
//        return super.getItemViewType(position)
//    }

    override fun getItemCount(): Int {
        return interestList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView
        view.interest.text = interestList[position]

//        view.interest.background()
//
//        view.interest.setDebounceClickListener(View.OnClickListener {
//
//            if (selectedPosition) {
//                view.interest.setTextColor(
//                    ContextCompat.getColor(
//                        view.interest.context,
//                        R.color.colorBlack
//                    )
//                )
//                view.interest.background = ContextCompat.getDrawable(
//                    view.interest.context,
//                    R.drawable.bg_rounded_white
//                )
//                selectedPosition = false
//
//            } else {
//                view.interest.setTextColor(
//                    ContextCompat.getColor(
//                        view.interest.context,
//                        R.color.colorPrimary
//                    )
//                )
//                view.interest.background = ContextCompat.getDrawable(
//                    view.interest.context,
//                    R.drawable.layout_rounded_outer_voilet
//                )
//                selectedPosition = true
//            }
//        })


//        when (holder) {
//            is InterestHeadingViewHolder -> {
//                view.interestHeading.text = interestList[position]
//
////                /* Adapter of Description points in fragment*/
////                val value = 2
////                val adp = InterestAdapter(value, interestList, interestListSports)
////                view.interestRecyclerView.adapter = adp
//            }
//            is InterestViewHolder -> {
//                view.interest.text = interestListSports[position]
//
//                view.interest.setDebounceClickListener(View.OnClickListener {
//                    if (selectedPosition) {
//                        view.interest.setTextColor(
//                            ContextCompat.getColor(
//                                view.interest.context,
//                                R.color.colorBlack
//                            )
//                        )
//                        view.interest.background = ContextCompat.getDrawable(
//                            view.interest.context,
//                            R.drawable.bg_rounded_white
//                        )
//                        selectedPosition = false
//
//                    } else {
//                        view.interest.setTextColor(
//                            ContextCompat.getColor(
//                                view.interest.context,
//                                R.color.colorPrimary
//                            )
//                        )
//                        view.interest.background = ContextCompat.getDrawable(
//                            view.interest.context,
//                            R.drawable.layout_rounded_outer_voilet
//                        )
//                        selectedPosition = true
//                    }
//                })
//            }
//        }
    }

    inner class InterestHeadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

//    inner class InterestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//    }
}