package com.example.emafoods.core.data.stringdecoder

import android.net.Uri
import com.example.emafoods.core.presentation.stringdecoder.StringDecoder
import javax.inject.Inject

class UriDecoder @Inject constructor() : StringDecoder {
    override fun decodeString(encodedString: String): String = Uri.decode(encodedString)
}