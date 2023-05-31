package com.example.emafoods.core.extension

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.emafoods.feature.addfood.data.compressimage.getFilePathFromUri
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import java.io.File
import java.util.Locale

fun String.capitalizeWords(): String = split(" ").map { it.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.ROOT
    ) else it.toString()
} }.joinToString(" ")

fun Uri.toByteArray(): ByteArray {
    val inputStream = this.toString().byteInputStream()
    return inputStream.readBytes()
}

suspend fun Uri.getCompressedImage(context: Context): Uri {
    val fileUri = getFilePathFromUri(this, context)
    val compressedImageFile = Compressor.compress(context, File(fileUri?.path.toString())) {
        quality(50)
        size(maxFileSize = 600000)
        format(Bitmap.CompressFormat.JPEG)
    }
    return Uri.fromFile(compressedImageFile)
}