package com.droidinsight.catalog.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.droidinsight.catalog.R
import com.droidinsight.catalog.extensionFunction.gotoFinish
import com.droidinsight.catalog.ui.home.HomeActivity
import statusBarTransparent

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT1: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
//        sharedPreferenceWriter = SharedPrefrencesWriter.getInstance(this)
//        splashLogo.visibility = View.VISIBLE
        Handler().postDelayed({
            gotoFinish(HomeActivity::class.java)
//            if (sharedPreferenceWriter.getBoolean(SharedPrefrencesKeys.FIRST_TIME_INAPP)!!) {
//                if (userViewModel.getUserData() != null) {
//                    userViewModel.getUserData().let {
//                        if (it?.isProfileCreated == "1" && it.is_verified == "1") {
//                            gotoFinish(HomeActivity::class.java)
//                            finishAffinity()
//                        } else {
//                            gotoFinish(SelectionActivity::class.java)
//                        }
//                    }
//                } else {
//                    gotoFinish(WalkScreenActivity::class.java)
//                }
//            } else {
//                gotoFinish(WalkScreenActivity::class.java)
//            }
        }, SPLASH_TIME_OUT1)
//        Handler().postDelayed({
//            if (sharedPreferenceWriter.getBoolean(SharedPrefrencesKeys.FIRST_TIME_INAPP)!!) {
//                if (sharedPreferenceWriter.getBoolean(SharedPrefrencesKeys.IS_PROFILE)!! && (sharedPreferenceWriter.getBoolean(
//                        SharedPrefrencesKeys.IS_OTP_VERIFIED
//                    )!!)
//                ) {
//                    gotoFinish(HomeActivity::class.java)
//                    finishAffinity()
//                } else {
////                    gotoFinish(SelectionActivity::class.java)
//                    if ((sharedPreferenceWriter.getBoolean(SharedPrefrencesKeys.IS_OTP_VERIFIED)!!)) {
//                        gotoFinish(ProfileCreateActivity::class.java)
//                    } else {
//                        gotoFinish(SelectionActivity::class.java)
//                    }
//                }
//            } else {
//                gotoFinish(WalkScreenActivity::class.java)
//            }
//        }, SPLASH_TIME_OUT1)

    }

}
