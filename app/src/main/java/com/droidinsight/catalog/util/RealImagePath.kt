package com.droidinsight.catalog.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils

import java.io.*

class RealImagePath {

    companion object {
        fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
            var contentUri = contentUri
            var cursor: Cursor?
            var filePath: String? = ""
            if (contentUri == null)
                return filePath

            val file = File(contentUri.path)
            if (file.exists())
                filePath = file.path
            if (!TextUtils.isEmpty(filePath))
                return filePath
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val wholeID = DocumentsContract.getDocumentId(contentUri)
                    // Split at colon, use second item in the array
                    //                String[] split = wholeID.split(":");
                    val id: String
                    if (wholeID.contains(":"))
                        id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    else
                        id = wholeID
                    //                if (split.length > 1)
                    //                    id = split[1];
                    //                else id = wholeID;
                    // where id is equal to
                    cursor = context.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        proj,
                        MediaStore.Images.Media._ID + "='" + id + "'",
                        null,
                        null
                    )
                    if (cursor != null) {
                        val columnIndex = cursor.getColumnIndex(proj[0])
                        if (cursor.moveToFirst())
                            filePath = cursor.getString(columnIndex)
                        if (!TextUtils.isEmpty(filePath))
                            contentUri = Uri.parse(filePath)
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }

            }
            if (!TextUtils.isEmpty(filePath))
                return filePath
            try {
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                if (cursor == null)
                    return contentUri.path
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst())
                    filePath = cursor.getString(column_index)
                if (!cursor.isClosed)
                    cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
                filePath = contentUri!!.path
            }

            if (filePath == null)
                filePath = ""
            return filePath
        }


        fun compressImageFile(pathUri: Uri, context: Context): File {
            var b: Bitmap? = null
            var realPath: String? = getRealPathFromURI(context, pathUri)
            var f = File(realPath)
            var o: BitmapFactory.Options = BitmapFactory.Options();
            o.inJustDecodeBounds = true

            var fis: FileInputStream
            try {
                fis = FileInputStream(f);
                BitmapFactory.decodeStream(fis, null, o)
                fis.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val IMAGE_MAX_SIZE = 1024;
            var scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = Math.pow(
                    2.0,
                    Math.ceil(Math.log(IMAGE_MAX_SIZE / Math.max(o.outHeight, o.outWidth).toDouble()) / Math.log(0.5))
                ).toInt();
            }

            var o2: BitmapFactory.Options = BitmapFactory.Options();
            o2.inSampleSize = scale;
            try {
                fis = FileInputStream(f);
                b = BitmapFactory.decodeStream(fis, null, o2);
                fis.close();
            } catch (e: FileNotFoundException) {
                e.printStackTrace();
            } catch (e: IOException) {
                e.printStackTrace();
            }


            var destFile = File(getImageFilePath());
            try {
                var out: FileOutputStream = FileOutputStream(destFile);
                b?.compress(Bitmap.CompressFormat.PNG, 60, out);
                out.flush();
                out.close();

            } catch (e: Exception) {
                e.printStackTrace();
            }
            return destFile;
        }

        fun getImageFilePath(): String {
            val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/fluper");
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.getAbsolutePath() + "/IMG_" + System.currentTimeMillis() + ".jpg"
        }

        fun getImageUri(context: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver, inImage,
                "Title", null
            )
            return Uri.parse(path)
        }
    }

    /*fun createRequestBody(value: String): RequestBody {
            return value.toRequestBody("text/plain".toMediaType())
        }*/
}