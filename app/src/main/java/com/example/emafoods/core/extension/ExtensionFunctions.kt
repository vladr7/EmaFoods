package com.example.emafoods.core.extension

import android.net.Uri
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