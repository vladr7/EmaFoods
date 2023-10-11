package com.example.emafoods.feature.addfood.presentation.image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

@Composable
fun imageCropLauncher(onImageCropped: (Uri) -> Unit, onCropError: (Int) -> Unit) =
    rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { onImageCropped(it) }
                ?: onCropError(9002)
        } else {
            onCropError(9002)
        }
    }

object CustomCropImageContractOptions {
    fun getDefaultOptions(imageUri: Uri) = CropImageContractOptions(
        imageUri,
        CropImageOptions(
            cropShape = CropImageView.CropShape.RECTANGLE,
            showProgressBar = false,
            initialCropWindowPaddingRatio = 0.2f,
            fixAspectRatio = true,
            aspectRatioX = 13,
            aspectRatioY = 10
        )
    )
}