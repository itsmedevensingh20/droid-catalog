import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns.PHONE
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.droidinsight.catalog.BuildConfig
import com.droidinsight.catalog.R
import com.droidinsight.catalog.constant.Constants
import com.droidinsight.catalog.listener.HomeListener
import com.droidinsight.catalog.util.SnackbarUtils
import com.nabinbhandari.android.permissions.PermissionHandler
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

var toast: Toast? = null
fun AppCompatActivity.showToast(message: String?) {
    if (toast != null) toast!!.cancel()
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    if (toast != null) toast!!.cancel()
    Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
}


fun AppCompatActivity.changeFragment(
    fragment: Fragment,
    container: Int,
    animation: Boolean,
    towardRight: Boolean
) {
    val transaction = supportFragmentManager.beginTransaction()
    if (animation) {
        if (towardRight) {
            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        } else {
            transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
    }
    transaction.replace(container, fragment).commit()
}

/*var gson: Gson? = null
fun getGsonInstance(): Gson {

    if (gson == null)
        gson = Gson()
    return gson!!
}*/

fun View.doBack() {
    if (this != null) {
        setOnClickListener {
            if (this.context is Activity) {
                var activity = this.getContext() as Activity
                activity.onBackPressed()
            }
        }
    }
}


fun View.doVisible() {
    if (this != null) {
        visibility = View.VISIBLE
    }
}

fun View.doGone() {
    if (this != null) {
        visibility = View.GONE
    }
}

fun EditText.getString(): String {
    return this.text.toString().trim()

}

fun ObservableField<String>.getString(): String {
    return this.get().toString()
}

fun TextView.getString(): String {
    return this.text.toString().trim()
}

fun String.getString(): String {
    return this.trim()
}

fun EditText.clear() {
    this.setText("")
}


fun Activity.openAppInGooglePlay() {

    val appId = BuildConfig.APPLICATION_ID
    try {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")))
    } catch (anf: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            )
        )
    }
}
/*
val String.isValidMobile: Boolean
    get() = if (this.length in 6..14)
        true
    else {
        this.matches(Constants.NUMBER_PATTERN.toRegex())

    }
*/


/*
fun Activity.isHasPermission(vararg permissions: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        permissions.all { singlePermission ->
            applicationContext.checkSelfPermission(singlePermission) == PackageManager.PERMISSION_GRANTED
        }
    else true
}
fun Activity.askPermission(vararg permissions: String, @IntRange(from = 0) requestCode: Int) =
    ActivityCompat.requestPermissions(this, permissions, requestCode)

*/

/*fun checkPermission(context: Context, vararg permission: String)
{
    Permissions.check(context, permission, null, null,
        object : PermissionHandler() {
            override fun onGranted() {

            }

        })
}*/
val String.isValidMobile: Boolean
    get() = PHONE.matcher(this).matches() && this.length in 8..14

val String.isValidPassword: Boolean
    get() = this.length in 8..16


val String.isValidEmail: Boolean
    get() = this.matches(Constants.EMAIL_PATTERN.toRegex())


fun AppCompatActivity.statusBarWhite() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

/*
fun AppCompatActivity.statusColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.window.statusBarColor = ContextCompat.getColor(this, R.color.statusColor)
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}
*/
fun getCurrentTimeStamp(): String {
    return (System.currentTimeMillis()).toString()

}


@SuppressLint("MissingPermission")
fun checkInternet(context: Context): Boolean {
    var status: Boolean = false
    try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        status = netInfo != null && netInfo.isConnected
        if (!status)
            Toast.makeText(
                context,
                context.resources.getString(R.string.error_internet),
                Toast.LENGTH_SHORT
            ).show()

        return status
    } catch (e: NullPointerException) {
        e.printStackTrace()
        return false
    }
}

fun checkInternetSnackBar(context: Context, view: View): Boolean {
    var status: Boolean = false
    try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        status = netInfo != null && netInfo.isConnected
        if (!status)
            SnackbarUtils.displayError(view)
        return status
    } catch (e: NullPointerException) {
        e.printStackTrace()
        return false
    }
}

fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String): Bitmap {
    val ei: ExifInterface = ExifInterface(image_absolute_path)
    val orientation: Int = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    );

    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> {
            return rotate(bitmap, 90);
        }
        ExifInterface.ORIENTATION_ROTATE_180 -> {
            return rotate(bitmap, 180);
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> {
            return rotate(bitmap, 270);
        }
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
            return flip(bitmap, false, true);
        }
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
            return flip(bitmap, true, false);
        }
        else -> {
            return bitmap
        }
    }
}

fun rotate(bitmap: Bitmap, degrees: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat());
    return Bitmap.createBitmap(
        bitmap, 0, 0, bitmap.getWidth(),
        bitmap.getHeight(), matrix, true
    );
}

fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {

    val matrix = Matrix()
    matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
    return Bitmap.createBitmap(
        bitmap, 0, 0, bitmap.getWidth(),
        bitmap.getHeight(), matrix, true
    )
}

/*fun BaseActivity.loadPic(iv: ImageView, url: String) {
    val options = com.bumptech.glide.request.RequestOptions()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
//        .priority(RenderScript.Priority.HIGH)

    com.bumptech.glide.Glide.with(this).load(url)
        .apply(options)
        .into(iv)
}*/

fun getScreenWidth(activity: Activity): Int {

    val display = activity.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)

    return outMetrics.widthPixels
}


fun bitmapToFile(bitmap: Bitmap, context: Context?): Uri {
    // Get the context wrapper
    val wrapper = ContextWrapper(context)

    // Initialize a new file instance to save bitmap object
    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file, "${UUID.randomUUID()}.jpg")

    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // Return the saved bitmap uri
    return Uri.parse(file.absolutePath)
}

fun getScreenHeight(activity: Activity): Float {

    var display = activity.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)

    return outMetrics.heightPixels.toFloat()
}

fun openCalendar(textView: TextView) {
    val myCalendar = Calendar.getInstance()
    val date = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        textView.text = sdf.format(myCalendar.time)
    }
    DatePickerDialog(
        textView.context, date, myCalendar
            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)
    ).show()

}

fun askPermission(context: Context, permissionList: Array<String>, listener: HomeListener) {
    com.nabinbhandari.android.permissions.Permissions.check(context, permissionList,
        null,
        null,
        object : PermissionHandler() {
            override fun onGranted() {
                listener.onOk()
            }
        })
}


fun setDrawableColorFilter(drawable: Drawable, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        drawable.colorFilter = BlendModeColorFilter(
            color,
            BlendMode.SRC_ATOP
        )
    } else {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

//fun AppCompatActivity.showSpinnerItem(
//    spinner: Spinner,
//    array: ArrayList<String>
//) {
//    val spinnerArrayAdapter = ArrayAdapter<String>(
//        this,
//        R.layout.spinner_dropdown_item_layout,
//        array
//    )
//    spinner.adapter = spinnerArrayAdapter
//    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//        }
//
//        override fun onItemSelected(
//            parent: AdapterView<*>?,
//            view: View?,
//            position: Int,
//            id: Long
//        ) {
//            val tv = view as TextView
//            if (position == 0) {
//                tv.setTextColor(resources.getColor(R.color.colorBlack))
//            } else {
//                tv.setTextColor(resources.getColor(R.color.colorBlack))
//            }
//        }
//
//    }
//}
//

//fun getErrorMessage(exception: HttpException): String {
//    com.first_love.util.GsonUtil.mGsonInstance = Gson()
//    val errorBody = com.first_love.util.GsonUtil.mGsonInstance!!.fromJson(
//        exception.response()
//            .errorBody()?.string(), ErrorBean::class.java
//    )
//
//    return errorBody.message
//}

//fun AppCompatActivity.setAddress(tv: TextView, lat: Double, lng: Double) {
//    try {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        val addresses =
//            geocoder.getFromLocation(lat, lng, 1) as ArrayList<Address>?
//        if (addresses != null && addresses.size > 0) {
//            tv.text = addresses[0].getAddressLine(0)
//            Constants.user_address = addresses[0].getAddressLine(0)
//        }
//
//    } catch (e: Exception) {
//
//    }
//}

//fun Fragment.setAddressInFragment(tv: TextView, lat: Double, lng: Double)
//{
//    try {
//        val geocoder = Geocoder(this.activity, Locale.getDefault())
//        val addresses =
//            geocoder.getFromLocation(lat, lng, 1) as ArrayList<Address>?
//        if (addresses != null && addresses.size > 0) {
//            tv.text = addresses[0].getAddressLine(0)
//            Constants.user_address = addresses[0].getAddressLine(0)
//        }
//
//    } catch (e: Exception) {
//
//    }
//}

fun printLog(str: String, value: String) {
    Log.e("###", str + " = " + value)
}

fun AppCompatActivity.enableFullScreen() {
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun hideKeyboard(context: Context?) {
    if (context is Activity) {
        val focusedView = context.currentFocus
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            focusedView?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun EditText.checkEmpty(errorMessage: String): Boolean {
    if (this.getString().isEmpty()) {
        this.error = errorMessage
        return false
    }
    return true
}

fun String.checkEmpty(): Boolean {
    if (this.getString().isEmpty()) {
        return true
    }
    return false
}

fun View.setDebounceClickListener(listener: View.OnClickListener, waitMillis: Long = 1000) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        if (System.currentTimeMillis() > lastClickTime + waitMillis) {
            listener.onClick(view)
            lastClickTime = System.currentTimeMillis()
        }
    }
}

/*  changePhoneNumber.setOnTouchListener(object :
        OnSwipeTouchListener(this@ChangePhoneNumberActivity) {
        override fun onSwipeRight() {
            viewModel.changePhoneNumberApi()
        }

    })*/


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun TextView.drawableChange(
    conditionCheck: Boolean,
    drawable: Int,
    falseDrawable: Int,
    start: Boolean,
    end: Boolean,
    top: Boolean,
    bottom: Boolean
) {
    if (conditionCheck) {
        when {
            start -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, 0, 0, 0)
            end -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawable, 0)
            top -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, drawable, 0, 0)
            bottom -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, drawable)
        }
    } else {
        when {
            start -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(falseDrawable, 0, 0, 0)
            end -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, falseDrawable, 0)
            top -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, falseDrawable, 0, 0)
            bottom -> this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, falseDrawable)
        }
    }
    this.compoundDrawablePadding = 8
}


//fun requestImageMultiPartArray(
//    imageList: ArrayList<String>,
//    createFormDataName: String
//): ArrayList<MultipartBody.Part> {
//    val postImagesParts = ArrayList<MultipartBody.Part>()
//    postImagesParts.clear()
//    for (i in 0 until imageList.size) {
//        val imageFilePath = File(imageList[i])
//        imageFilePath.exists().let {
//            val requestFile = imageFilePath.asRequestBody("image/*".toMediaTypeOrNull())
//            postImagesParts.add(
//                MultipartBody.Part.createFormData(
//                    createFormDataName, imageFilePath.name,
//                    requestFile
//                )
//            )
//        }
//
//    }
//    return postImagesParts
//}

//fun getImageInMultiPart(
//    compressedImgPath: String,
//    createFormDataName: String
//): MultipartBody.Part
//? {
//    val file = File(compressedImgPath)
//    file.exists().let {
//        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//        return MultipartBody.Part.createFormData(
//            createFormDataName, file.name,
//            requestFile
//        )
//    }
//}
//
//fun View.showSnackBar(message: String?) {
//    if (message == null) return
//    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
//    val tv =
//        snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
//    tv.setTextColor(Color.WHITE)
//    snackBar.show()
//}
//
//fun checkOtp(
//    context: Context?,
//    otp1: ObservableField<String>,
//    otp2: ObservableField<String>,
//    otp3: ObservableField<String>,
//    otp4: ObservableField<String>
//
//): Boolean {
//    if (otp1.get()?.getString()?.isEmpty()!!
//        && otp2.get()?.getString()?.isEmpty()!!
//        && otp3.get()?.getString()?.isEmpty()!!
//        && otp4.get()?.getString()?.isEmpty()!!
//    ) {
//        Toast.makeText(
//            context,
//            context?.resources?.getString(R.string.error_otp),
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//    if (otp1.get()?.getString()?.isEmpty()!!) {
//        Toast.makeText(
//            context,
//            context?.resources?.getString(R.string.error_otp),
//            Toast.LENGTH_SHORT
//        ).show()
//        return false
//
//    }
//    if (otp2.get()?.getString()?.isEmpty()!!) {
//        Toast.makeText(
//            context,
//            context?.resources?.getString(R.string.error_otp),
//            Toast.LENGTH_SHORT
//        ).show()
//        return false
//
//    }
//    if (otp3.get()?.getString()?.isEmpty()!!) {
//        Toast.makeText(
//            context,
//            context?.resources?.getString(R.string.error_otp),
//            Toast.LENGTH_SHORT
//        ).show()
//        return false
//
//    }
//    if (otp4.get()?.getString()?.isEmpty()!!) {
//        Toast.makeText(
//            context,
//            context?.resources?.getString(R.string.error_otp),
//            Toast.LENGTH_SHORT
//        ).show()
//        return false
//    }
//    return true
//}

fun AppCompatActivity.setAddress(tv: TextView, lat: Double, lng: Double) {
    try {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(lat, lng, 1) as java.util.ArrayList<Address>?
        if (addresses != null && addresses.size > 0) {
            tv.text = addresses[0].getAddressLine(0)
//            Constants.user_address = addresses[0].getAddressLine(0)
        }

    } catch (e: Exception) {

    }
}

fun twoValuesAfterDecimal(value: Double?): String {
    val formatter = DecimalFormat("##.00")
    return ("${formatter.format(value)}")
}

fun getTimeStampConvert(timeStamp: Long): String {
    val sdf = SimpleDateFormat("d MMM yyyy , hh:mm aa", Locale.getDefault())
    return sdf.format(Date(timeStamp))
}

fun getFileExtension(uri: Uri, context: Context): String? {
    val cr = context.contentResolver
    val mime = MimeTypeMap.getSingleton()
    return mime.getExtensionFromMimeType(cr.getType(uri))
}

fun convertKmToMiles(km: String?): Double {
    return km?.toDouble()?.times(0.621371)!!
}

//
//fun AppCompatActivity.showVerifiedMessageDialog(
//    dialogHeading: String, message: String, okText: String, listener: HomeListener
//) {
//    val dialog = Dialog(this)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialog.setContentView(R.layout.layout_verified_account)
//    dialog.setCancelable(false)
//    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    dialog.dialogHeading.text = dialogHeading
//    dialog.dialogDesc.text = message
//    dialog.accountVerifiedDone.text = okText
//    dialog.accountVerifiedDone.setOnTouchListener(object :
//        OnSwipeTouchListener(applicationContext) {
//        override fun onSwipeRight() {
//            dialog.dismiss()
//            listener.onOk()
//        }
//
//    })
//
//    dialog.show()
//}
//
//fun AppCompatActivity.showCancellableMessageDialog(
//    message: String, okText: String, listener: HomeListener
//) {
//    val dialog = Dialog(this)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialog.setContentView(R.layout.layout_created_successfully)
//    dialog.setCancelable(false)
//    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    dialog.moreAddDesc.text = message
//    dialog.yes_tv.text = okText
////    dialog.no_tv.setOnClickListener {
////        dialog.dismiss()
////        listener.onCancel()
////    }
//    dialog.yes_tv.setOnClickListener {
//        dialog.dismiss()
//        listener.onOk()
//    }
//    dialog.show()
//}
//
//fun AppCompatActivity.showPictureMatchingMessageDialog(
//    pictureMatchDesc: String,
//    pictureMatchHeading: String,
//    pictureMatchDone: String,
//    url: String,
//    imageString: Uri?,
//    listener: HomeListener
//) {
//    val dialog = Dialog(this)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialog.setContentView(R.layout.layout_picture_match)
//    dialog.setCancelable(false)
//    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    dialog.pictureMatchDesc.text = pictureMatchDesc
//    dialog.pictureMatchHeading.text = pictureMatchHeading
//    dialog.pictureMatchDone.text = pictureMatchDone
//    Glide.with(this)
//        .load(url)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .transform(FitCenter(), RoundedCorners(10))
//        .into(dialog.poseImage)
//    Glide.with(this)
//        .load(imageString)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .transform(FitCenter(), RoundedCorners(10))
//        .into(dialog.clickFromCameraImage)
//
//    dialog.reTakeText.setOnClickListener {
//        dialog.dismiss()
//        listener.onCancel()
//    }
//    dialog.pictureMatchDone.setOnClickListener {
//        dialog.dismiss()
//        listener.onOk()
//    }
//    dialog.show()
//}

fun checkString(str: String): Boolean {
    return str.contains("/user/")
}
//
//fun AppCompatActivity.showLogoutDialog(
//    message: String, okText: String, noText: String, listener: HomeListener
//) {
//    val dialog = Dialog(this)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialog.setContentView(R.layout.layout_logout)
//    dialog.setCancelable(false)
//    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    dialog.moreAddDesc.text = message
//    dialog.yes_tv.text = okText
//    dialog.no_tv.text = noText
//    dialog.no_tv.setOnClickListener {
//        dialog.dismiss()
//        listener.onCancel()
//    }
//    dialog.yes_tv.setOnClickListener {
//        dialog.dismiss()
//        listener.onOk()
//    }
//    dialog.show()
//}

/*fun EditText.checkEmail(): Boolean {
    if (this.getString().isEmpty()) {
        this.error = resources.getString(R.string.error_email)
        this.requestFocus()
        return false
    }
    if (!this.getString().isValidEmail) {
        this.error = resources.getString(R.string.error_valid_email)
        this.requestFocus(this.length())
        return false
    }
    return true
}

fun EditText.checkMobile(): Boolean {
    if (this.getString().isEmpty()) {
        this.error = resources.getString(R.string.error_mobile)
        this.requestFocus()
        return false
    }
    if (!this.getString().isValidMobile) {
        this.error = resources.getString(R.string.error_valid_mobile)
        this.requestFocus(this.length())
        return false
    }
    return true
}

fun EditText.checkPassword(): Boolean {
    if (this.getString().isEmpty()) {
        this.error = resources.getString(R.string.error_password)
        this.requestFocus()
        return false
    }
    if (!this.getString().isValidPassword) {
        this.error = resources.getString(R.string.error_valid_password)
        this.requestFocus(this.length())
        return false
    }
    return true
}

fun matchPassword(edt1: EditText, edt2: EditText): Boolean {

    if (edt1.getString().isEmpty()) {
        edt1.error = edt1.context.resources.getString(R.string.error_password)
        edt1.requestFocus()
        return false
    }
    if (!edt1.getString().isValidPassword) {
        edt1.error = edt1.context.resources.getString(R.string.error_valid_password)
        edt1.requestFocus(edt1.length())
        return false
    }
    if (edt2.getString().isEmpty()) {
        edt2.error = edt2.context.resources.getString(R.string.error_confirm_password)
        edt2.requestFocus()
        return false
    }
    if (!edt1.getString().equals(edt2.getString())) {
        edt2.error = edt2.context.resources.getString(R.string.error_password_does_not_match)
        edt2.requestFocus(edt2.length())
        return false
    }
    return true
}



fun String.checkEmpty(): Boolean {
    if (this.getString().isEmpty()) {
        return true
    }
    return false
}

fun ObservableField<String>.checkName(context: Context?, error: ObservableField<String>?): Boolean {
    error?.set(null)
    if (this.get()!!.getString().isEmpty()) {
        error?.set(context!!.resources.getString(R.string.error_name))
        return false
    }
    return true
}

fun EditText.checkName(): Boolean {
    if (this.getString().isEmpty()) {
        this.error = resources.getString(R.string.error_name)
        this.requestFocus()
        return false
    }
    return true
}

fun CheckBox.checkTermsCondition(context: Context?): Boolean {
    if (!this.isChecked) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(
            context,
            resources.getString(R.string.error_terms_conditions),
            Toast.LENGTH_SHORT
        )
        toast!!.show()
        return false
    }
    return true
}
*/
fun AppCompatActivity.statusBarTransparent() {
    if (Build.VERSION.SDK_INT in 19..20) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                true
            )
        }
    }
    if (Build.VERSION.SDK_INT >= 19) {
        this.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(
            this,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            false
        )
        this.window.statusBarColor = Color.TRANSPARENT
    }
}

fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

/*fun checkOtp(
    context: Context?,
    otp1: ObservableField<String>,
    otp2: ObservableField<String>,
    otp3: ObservableField<String>,
    otp4: ObservableField<String>,
    errorOtp1: ObservableField<String>?,
    errorOtp2: ObservableField<String>?,
    errorOtp3: ObservableField<String>?,
    errorOtp4: ObservableField<String>?

): Boolean {

    if (otp1.get()?.getString()?.isEmpty()!!
        && otp2.get()?.getString()?.isEmpty()!!
        && otp3.get()?.getString()?.isEmpty()!!
        && otp4.get()?.getString()?.isEmpty()!!
    ) {
        errorOtp1?.set(context?.resources?.getString(R.string.error_otp))
        errorOtp2?.set(context?.resources?.getString(R.string.error_otp))
        errorOtp3?.set(context?.resources?.getString(R.string.error_otp))
        errorOtp4?.set(context?.resources?.getString(R.string.error_otp))
    }
    if (otp1.get()?.getString()?.isEmpty()!!) {
        errorOtp1?.set(context?.resources?.getString(R.string.error_otp))
        return false

    }
    if (otp2.get()?.getString()?.isEmpty()!!) {
        errorOtp2?.set(context?.resources?.getString(R.string.error_otp))
        return false

    }
    if (otp3.get()?.getString()?.isEmpty()!!) {
        errorOtp3?.set(context?.resources?.getString(R.string.error_otp))
        return false

    }
    if (otp4.get()?.getString()?.isEmpty()!!) {
        errorOtp4?.set(context?.resources?.getString(R.string.error_otp))
        return false

    }

    return true
}*/
