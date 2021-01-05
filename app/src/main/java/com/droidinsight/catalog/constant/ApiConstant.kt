package com.droidinsight.catalog.constant

interface ApiConstant {
    companion object {
        const val BASE_URL = "http://firstlove.fluper.in:3000/user/"

        const val IMAGE_URL = "http://firstlove.fluper.in:3000"


        /**Apis**/

        const val user_signup = "user_signup"
        const val verify = "verify"
        const val resend_otp = "resend_otp"
        const val FORGETPASSWORD = "forget_password"
        const val RESETPASSWORD = "reset_password"
        const val LOGIN = "login"
        const val PROFILECREATE = "complete_profile"
        const val SOCIALSIGNUP = "social-singup"
        const val get_profile = "get_profile"
        const val PROFILEUPDATE = "update_profile"
        const val UPDATEMOBILENUMBER = "update_mobile_number"
        const val FEEDBACK = "feedback"
        const val GET_VERIFIED_IMAGES = "getVerifiedImage"
        const val SEND_VERIFIED_IMAGES = "verifiedImage"
        const val HELP_CENTER_URL = "/user/get_faq_data"
        const val SETTING_UPDATE = "update_setting"
        const val SETTING_DETAILS = "get_profile_setting"
        const val UPDATE_NOTIFICATION  = "updateNotification"
        const val UPDATE_GHOST  = "updateGhotsMode"
        const val VERIFIED_CHANGE_OTP  = "verify_updated_mobile_number_otp"


        /**New Api**/
        const val mobileLogin = "mobileSignup"

        /**Home Api**/
        const val getAllCategoryItems = "getCategory"
        const val getMenuItems = "getMenuItems"
        const val getTagProduct = "getTagProduct"
        const val getRelatedProduct = "getRelatedProduct"
        const val getSearchProduct = "searchProduct"
        const val getRecentSearch = "RecentSearch"
        const val getRecipesDetails = "get_recipe_details"
        const val sort_product = "sort_product"
        const val getCartItem = "get_cart_item"
        const val addToCart = "add_to_cart"
        const val addToFav = "add_to_fav"
        const val getFav = "get_fav_data"
        const val addRecipeCart = "add_recipe_cart"
        const val removeCartItem = "remove_cart_item"
        const val getAddress = "get_address"
        const val specific_address_time = "specific_address_time"
        const val save_address = "save_address"
        const val delete_address = "delete_address"
        const val placeOrder = "save_order"
        const val ongoing_order = "ongoing_order"
        const val user_cancel_order = "user_cancel_order"
        const val rating_review = "rating_review"
        const val reorder = "reorder"
        const val chatImageUrl = "chatImageUrl"
        const val get_offer = "get_offer"
        const val addToCartDealsOffer = "addToCartDealsOffer"

    }


}