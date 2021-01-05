package com.droidinsight.catalog.response

data class InterestCommonBeam(
    var message: String,
    var result: ArrayList<Result>
) {
    data class Result(
        var details: ArrayList<Detail>,
        var type: String
    ) {
        data class Detail(
            var created_at: String,
            var deleted_at: String,
            var id: Int?,
            var image: String,
            var language: String,
            var technology: String,
            var type: Int?,
            var updated_at: String
        )
    }
}