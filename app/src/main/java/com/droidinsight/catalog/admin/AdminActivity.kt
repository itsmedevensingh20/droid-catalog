package com.droidinsight.catalog.admin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import askPermission
import checkInternetSnackBar
import clear
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.droidinsight.catalog.R
import com.droidinsight.catalog.base.BaseActivity
import com.droidinsight.catalog.constant.Constants
import com.droidinsight.catalog.databinding.ActivityAdminBinding
import com.droidinsight.catalog.firebase.DROID_INSIGHT
import com.droidinsight.catalog.firebase.TOP_POST
import com.droidinsight.catalog.firebase.beam.TopPostBeam
import com.droidinsight.catalog.listener.HomeListener
import com.droidinsight.catalog.util.Glide4Engine
import com.droidinsight.catalog.util.RealPathUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import getFileExtension
import getString
import getTimeStampConvert
import kotlinx.android.synthetic.main.activity_admin.*
import setDebounceClickListener
import showToast
import statusBarTransparent

class AdminActivity : BaseActivity<ActivityAdminBinding, AdminViewModel>(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        bindViewModel()
        init()
        initControl()
    }


    private lateinit var imagePathUri: Uri
    override val layoutRes: Int
        get() = R.layout.activity_admin
    override val viewModelClass: Class<AdminViewModel>
        get() = AdminViewModel::class.java


    private lateinit var firebaseDatabaseReference: DatabaseReference
    private lateinit var topPostDatabaseReference: DatabaseReference

    private lateinit var firebaseStorageReference: StorageReference
//    private lateinit var topPostStorageReference: StorageReference


    override fun bindViewModel() {
        binding.adminViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        binding.uploadTopPost.setDebounceClickListener(this)
        binding.selectTopPost.setDebounceClickListener(this)
    }

    override fun initControl() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        topPostDatabaseReference = firebaseDatabaseReference.child(DROID_INSIGHT).child(TOP_POST)

        firebaseStorageReference = FirebaseStorage.getInstance().reference
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.selectTopPost -> {
                galleryCall()
            }
            R.id.uploadTopPost -> {
                if (checkInternetSnackBar(this, binding.adminParent)) {
                    with(binding.typeMessage.getString())
                    {
                        if (isNotEmpty())
                            uploadToFireStore(
                                imagePathUri = imagePathUri,
                                imageUri = binding.typeURL.getString(),
                                imageName = this
                            )
                    }
                }
            }
        }
    }


    private fun uploadToFireStore(
        imagePathUri: Uri,
        imageUri: String = "",
        imageName: String = ""
    ) {
        showProgress()
        firebaseStorageReference.child(
            "${imageName}_TOP_POST at ${getTimeStampConvert(System.currentTimeMillis())}.${
                getFileExtension(
                    imagePathUri,
                    this
                )
            }"
        ).putFile(imagePathUri)
            .addOnSuccessListener {
                firebaseStorageReference.child(
                    "${imageName}_TOP_POST at ${getTimeStampConvert(System.currentTimeMillis())}.${
                        getFileExtension(
                            imagePathUri,
                            this
                        )
                    }"
                ).downloadUrl.addOnSuccessListener { its ->
                    hideProgress()
                    uploadToFireBase(
                        imagePath = its.toString(),
                        imagePathUrl = imageUri
                    )

                }

            }.addOnFailureListener { its ->
                hideProgress()
                showToast("$its")
            }.addOnProgressListener {
                showProgress()
            }
    }


    private fun galleryCall() {
        val permissionList = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        askPermission(this, permissionList, object : HomeListener {
            override fun onOk() {
                selectImages()
            }
        })
    }

    private fun selectImages() {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .countable(true)
            .maxSelectable(1)
            .showPreview(true)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(Glide4Engine())
            .forResult(Constants.MatisseLib)
    }


    private fun uploadToFireBase(imagePath: String = "", imagePathUrl: String = "") {
        showProgress()
        val messageId = "${firebaseDatabaseReference.push().key}"
        val messageData = TopPostBeam(
            message_id = messageId,
            top_post_images = imagePath,
            top_post_link = imagePathUrl
        )
        topPostDatabaseReference.child(messageData.message_id.toString())
            .setValue(messageData)
            .addOnSuccessListener {
                hideProgress()
                showToast("Top Post uploaded")
                imagePathGallery = ""
                loadImage(imagePathGallery)
                with(binding.typeMessage)
                {
                    this.clear()
                }

            }
            .addOnFailureListener {
                hideProgress()
                showToast("Top Post failed")
            }
    }

    var imagePathGallery = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == Activity.RESULT_OK) {
            data.let {
                val mSelected = Matisse.obtainResult(data)
                for (uri in mSelected) {
                    imagePathUri = uri
                    val compressed = RealPathUtil.compressImageFile(uri, this)
                    val addImageToFeed = compressed.path
                    addImageToFeed.let { its ->
                        this.imagePathGallery = its
                        loadImage(imagePathGallery)
                    }
                }
            }
        }
    }

    private fun loadImage(imagePathGallery: String) {
        Glide.with(this)
            .load(imagePathGallery)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.bg_gradient_light_green)
            )
            .transform(FitCenter(), RoundedCorners(10))
            .into(binding.userImages)

    }
}