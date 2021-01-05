package com.droidinsight.catalog.firebase.beam

import android.os.ParcelFileDescriptor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TopPostBeam(
    var message_id: String? = "",
    var top_post_images: String? = "",
    var top_post_link: String? = ""
) : Parcelable