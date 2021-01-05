package com.droidinsight.catalog.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.droidinsight.catalog.R
import com.droidinsight.catalog.activity.WebViewActivity
import com.droidinsight.catalog.admin.AdminActivity
import com.droidinsight.catalog.base.BaseActivity
import com.droidinsight.catalog.constant.Constants
import com.droidinsight.catalog.databinding.ActivityHomeBinding
import com.droidinsight.catalog.extensionFunction.goToNext
import com.droidinsight.catalog.firebase.DROID_INSIGHT
import com.droidinsight.catalog.firebase.TOP_POST
import com.droidinsight.catalog.firebase.beam.TopPostBeam
import com.droidinsight.catalog.ui.androidTutorial.AndroidTutorialActivity
import com.droidinsight.catalog.util.ProgressDialogUtils
import com.first_love.constant.EXTRA_DATA
import com.first_love.constant.EXTRA_FLAG
import com.google.android.gms.ads.*
import com.google.firebase.database.*
import doGone
import doVisible
import getScreenWidth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.layout_home.*
import kotlinx.android.synthetic.main.layout_home_screan.*
import kotlinx.android.synthetic.main.layout_home_toolbar.*
import kotlinx.android.synthetic.main.layout_side_navigation.*
import openAppInGooglePlay
import setDebounceClickListener
import showToast
import statusBarTransparent


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), View.OnClickListener {

    private var adp: TopBlogAdapter = TopBlogAdapter()
    private var adp2: TutorialAdapter = TutorialAdapter()
    private lateinit var firebaseDatabaseReference: DatabaseReference
    private lateinit var topPostDatabaseReference: DatabaseReference
    private lateinit var adView: AdView
    private var dotsCount: Int = 0
    private var dots: Array<ImageView?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolBar)
        statusBarTransparent()
        bindViewModel()
        init()
        initControl()
        mInItDrawer()
    }


    override val layoutRes: Int
        get() = R.layout.activity_home
    override val viewModelClass: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    private val adSize: AdSize
        get() {
            val density = getScreenWidth(this)
            var adWidthPixels = ad_view_container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = DisplayMetrics().widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun bindViewModel() {
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
//        loadWebView()
        setAdapter()

        super.onStart()
    }

    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    private var eventListenerRemoved: Boolean = true

    override fun onResume() {
        adView.resume()
        super.onResume()
        if (eventListenerRemoved) {
            topPostDatabaseReference.addChildEventListener(childEventListener)
        }
    }


    override fun onStop() {
        super.onStop()
        eventListenerRemoved = true
        topPostDatabaseReference.removeEventListener(childEventListener)
    }


    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    override fun init() {
        menu_drawer.setDebounceClickListener(this)
        ourWebsite_nav.setDebounceClickListener(this)
        home_nav.setDebounceClickListener(this)
        exitApp.setDebounceClickListener(this)
        rateUs.setDebounceClickListener(this)
        share_nav.setDebounceClickListener(this)
        androidWidgetsCV.setDebounceClickListener(this)
        kotlinTutorial.setDebounceClickListener(this)
        aboutUs_nav.setDebounceClickListener(this)
        admin.setDebounceClickListener(this)
    }

    private var initialLayoutComplete = false

    override fun initControl() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        topPostDatabaseReference = firebaseDatabaseReference.child(DROID_INSIGHT).child(TOP_POST)

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) { }


//        MobileAds.setRequestConfiguration(
//            RequestConfiguration.Builder()
//                .setTestDeviceIds(listOf("AD2A68C627C91F1572F30CA74E8957CF"))
//                .build()
//        )


        adView = AdView(this)
        ad_view_container.addView(adView)
        ad_view_container.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
    }

    private fun loadBanner() {
        adView.adUnitId = getString(R.string.banner_home_footer)
        adView.adSize = adSize
        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this device."
        val adRequest = AdRequest
            .Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }

    private val topPostBeam = ArrayList<TopPostBeam>()

    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val message = p0.getValue(TopPostBeam::class.java)
            if (message != null) {
                // Get index of message received here
                // and update the list and notify the adapter
                var itemIndex = -1
                for (i in 0 until topPostBeam.size) {
                    if (message.message_id == topPostBeam[i].message_id) {
                        itemIndex = i
                        break
                    }
                }

                if (itemIndex != -1) {
                    topPostBeam[itemIndex] = message
                    adp.notifyItemChanged(itemIndex)
                    adp2.notifyItemChanged(itemIndex)
                }
            }

        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val message = p0.getValue(TopPostBeam::class.java)
            if (message != null) {
                Log.e("###", "onChildAdded")

                //Add this message to list if list do not contain it
                val messageExists = topPostBeam.any { it.message_id == message.message_id }
                if (!messageExists) {
                    topPostBeam.add(message)
                    adp.notifyItemInserted(topPostBeam.size)
                    adp2.notifyItemInserted(topPostBeam.size)
                }
            }
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            Log.e("###", " ChatActivity, onChildRemoved")
        }
    }

    private fun setAdapter() {
        adp2 = TutorialAdapter()
        tutorialRView.adapter = adp2
        adp2.setData(topPostBeam, this)

        adp = TopBlogAdapter()
        topPostViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        topPostViewPager.adapter = adp
        adp.setData(topPostBeam, this)

        with(topPostViewPager) {
            offscreenPageLimit = 2
            clipToPadding = false
            clipChildren = false
        }

        val pageMargin = 20
        val pageOffset = 30

        topPostViewPager.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if (topPostViewPager.orientation === ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(topPostViewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            } else {
                page.translationY = myOffset
            }
        }
//        /*Animation*/
//        val widthDp = (getScreenWidth(this) * 5 / 100)
//        topPostViewPager.setPadding(widthDp, 0, widthDp, 0)
//

        if (topPostBeam.size > 0) {
            dotsCount = 4
            dots = arrayOfNulls(dotsCount)

            for (i in 0 until dotsCount) {
                dots!![i] = ImageView(this)
                dots!![i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.unselect_pagination
                    )
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                params.setMargins(8, 0, 8, 0)

                sliderDots.addView(dots!![i], params)
            }
            dots!![0]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.select_pagination
                )
            )

            topPostViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    changeDot(position)
                }

            })
        }
    }

    private fun changeDot(position: Int) {
        for (i in 0 until dotsCount) {
            dots!![i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.unselect_pagination
                )
            )
        }
        dots!![position]?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.select_pagination
            )
        )
    }


    private fun mInItDrawer() {
        val actionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close) {
                private val scaleFactor = 10f
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = drawerView.width * slideOffset
                    coordinatorLayout.translationX = slideX
                    coordinatorLayout.scaleX = 1 - slideOffset / scaleFactor
                    coordinatorLayout.scaleY = 1 - slideOffset / scaleFactor
                    Log.e("###", (1 - slideOffset / scaleFactor).toString())
                }
            }
        drawer_layout.setScrimColor(Color.TRANSPARENT)
        drawer_layout.drawerElevation = 1f
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
    }

    private var websiteUrl: String = "https://droidinsight.com/"
    override fun onClick(view: View) {
        when (view.id) {
            R.id.androidWidgetsCV -> {
                goToNext(Intent(this, AndroidTutorialActivity::class.java), Constants.HOME_PAGE)
//                startActivityForResult(
//                    Intent(this, WebViewActivity::class.java).apply {
//                        putExtra(
//                            EXTRA_DATA,
//                            "https://droidinsight.com/android-development-tutorial/"
//                        )
//                        putExtra(EXTRA_FLAG, getString(R.string.app_name))
//                    },
//                    Constants.WEB_VIEWS
//                )
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.admin -> {
                goToNext(Intent(this, AdminActivity::class.java), Constants.HOME_PAGE)
//                startActivityForResult(
//                    Intent(this, WebViewActivity::class.java).apply {
//                        putExtra(
//                            EXTRA_DATA,
//                            "https://droidinsight.com/android-development-tutorial/"
//                        )
//                        putExtra(EXTRA_FLAG, getString(R.string.app_name))
//                    },
//                    Constants.WEB_VIEWS
//                )
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.kotlinTutorial -> {
                startActivityForResult(
                    Intent(this, WebViewActivity::class.java).apply {
                        putExtra(EXTRA_DATA, "https://droidinsight.com/flutter-tutorial/")
                        putExtra(EXTRA_FLAG, getString(R.string.app_name))
                    },
                    Constants.WEB_VIEWS
                )
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.menu_drawer -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                } else {
                    drawer_layout.openDrawer(GravityCompat.START)
                }
            }
            R.id.home_nav -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.ourWebsite_nav -> {
                startActivityForResult(
                    Intent(this, WebViewActivity::class.java).apply {
                        putExtra(EXTRA_DATA, websiteUrl)
                        putExtra(EXTRA_FLAG, getString(R.string.app_name))
                    },
                    Constants.WEB_VIEWS
                )
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.exitApp -> {
                finishAffinity()
            }
            R.id.rateUs -> {
                openAppInGooglePlay()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.aboutUs_nav -> {
                startActivityForResult(
                    Intent(this, WebViewActivity::class.java).apply {
                        putExtra(EXTRA_DATA, "https://droidinsight.com/about-us/")
                        putExtra(EXTRA_FLAG, getString(R.string.app_name))
                    },
                    Constants.WEB_VIEWS
                )

                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.share_nav -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id=com.droidinsight.catalog"
                    )
                }
                startActivity(sendIntent)
                drawer_layout.closeDrawer(GravityCompat.START)
            }

        }
    }

    private fun exitApp() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
                return
            } else {
                doubleBackToExitPressedOnce = true
                showToast(getString(R.string.back_press_exit))
                Handler().postDelayed(
                    { doubleBackToExitPressedOnce = false },
                    Constants.BACK_PRESS_TIME_INTERVAL

                )
            }
        }

    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        exitApp()
    }

    /* @SuppressLint("SetJavaScriptEnabled")
     private fun loadWebView() {
         latestBlogWebView.settings.javaScriptEnabled = true   // Enable Javascript
         latestBlogWebView.webViewClient = MyWebViewClient()
         val url = "https://droidinsight.com/blog/"
         ProgressDialogUtils().getInstance().showProgress(this)
         latestBlogWebView.doGone()
         url.let { latestBlogWebView.loadUrl(it) }
     }

     inner class MyWebViewClient : WebViewClient() {
         override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
             url?.let { view!!.loadUrl(it) }
             return true
         }

         override fun onPageFinished(view: WebView?, url: String?) {
             super.onPageFinished(view, url)
             ProgressDialogUtils().getInstance().hideProgress()
             latestBlogWebView.doVisible()
         }

         override fun onReceivedSslError(
             view: WebView?,
             handler: SslErrorHandler?,
             error: SslError?
         ) {
             val builder = AlertDialog.Builder(this@HomeActivity)
             builder.setMessage("Error in loading page")
             builder.setPositiveButton("continue") { _, _ -> handler!!.proceed() }
             builder.setNegativeButton("cancel") { _, _ -> handler!!.cancel() }
             val dialog = builder.create()
             dialog.show()
         }
     }*/

}
