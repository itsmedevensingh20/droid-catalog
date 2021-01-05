package com.droidinsight.catalog.constant

object Constants {

    const val ACCESS_FINE_LOCATION_INTENT_ID = 10001
    const val LOCATION_UPDATE = 10002

    const val BACK_PRESS_TIME_INTERVAL: Long = 2000

    // Formats
    const val EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val NUMBER_PATTERN = "[0-9]+"

    const val PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[A-Z]).{8,255}\$"
    const val NAME_PATTERN = "^[ A-Za-z_-]+$"
    const val MOBILE_PATTERN = "0-9"
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val DEVICE_TYPE = "0"

    const val AddPhotoImage = 30
    const val AddImages = 31


    const val MatisseLib = 1040
    const val CAMERA_CONSTANT = 1041
    const val GALLERY_CONSTANT = 1042

    const val INTEREST_HEADING = 201
    const val INTEREST_TEXT = 202
    const val UPDATE_NUMBER = 203
    const val VERIFYIMAGE = 204
    const val FEED_BACK = 205
    const val WEB_VIEWS = 206
    const val CHANGE_SETTING = 207
    const val HOME_PAGE = 208

}