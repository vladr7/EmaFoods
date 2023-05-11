package com.example.emafoods.core.data.models

import com.example.emafoods.core.data.models.helper.Model
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Food(
    @DocumentId
    @SerialName("id") val id: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("timeInSeconds") val addedDateInSeconds: Long = Timestamp.now().seconds,
    val imageRef: String = ""
): java.io.Serializable, Model()
