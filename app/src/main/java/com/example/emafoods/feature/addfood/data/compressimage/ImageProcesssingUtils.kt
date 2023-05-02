package com.example.emafoods.feature.addfood.data.compressimage

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import java.io.*

suspend fun getCompressedImage(imageUri: Uri, context: Context): Uri {
    val fileUri = getFilePathFromUri(imageUri, context)
    val compressedImageFile = Compressor.compress(context, File(fileUri?.path.toString())) {
        quality(50)
        size(maxFileSize = 600000)
        format(Bitmap.CompressFormat.JPEG)
    }
    return Uri.fromFile(compressedImageFile)
}


@Throws(IOException::class)
fun getFilePathFromUri(uri: Uri, context: Context?): Uri? {
    val fileName: String? = getFileName(uri, context)
    val file = fileName?.let { File(context?.externalCacheDir, it) }
    file?.createNewFile()
    FileOutputStream(file).use { outputStream ->
        context?.contentResolver?.openInputStream(uri).use { inputStream ->
            copyFile(inputStream, outputStream)
            outputStream.flush()
        }
    }
    return Uri.fromFile(file)
}

@Throws(IOException::class)
private fun copyFile(`in`: InputStream?, out: OutputStream) {
    val buffer = ByteArray(1024)
    var read: Int? = null
    while (`in`?.read(buffer).also { read = it } != -1) {
        read?.let { out.write(buffer, 0, it) }
    }
}

fun getFileName(uri: Uri, context: Context?): String {
    var fileName: String? = getFileNameFromCursor(uri, context)
    if (fileName == null) {
        val fileExtension: String? = getFileExtension(uri, context)
        fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""
    } else if (!fileName.contains(".")) {
        val fileExtension: String? = getFileExtension(uri, context)
        fileName = "$fileName.$fileExtension"
    }
    return fileName
}

fun getFileExtension(uri: Uri, context: Context?): String? {
    val fileType: String? = context?.contentResolver?.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

fun getFileNameFromCursor(uri: Uri, context: Context?): String? {
    val fileCursor: Cursor? = context?.contentResolver
        ?.query(uri, arrayOf<String>(OpenableColumns.DISPLAY_NAME), null, null, null)
    var fileName: String? = null
    if (fileCursor != null && fileCursor.moveToFirst()) {
        val cIndex: Int = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cIndex != -1) {
            fileName = fileCursor.getString(cIndex)
        }
    }
    return fileName
}