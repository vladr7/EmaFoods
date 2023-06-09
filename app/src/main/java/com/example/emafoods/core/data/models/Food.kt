package com.example.emafoods.core.data.models

import com.example.emafoods.core.data.models.helper.Model
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Food(
    @DocumentId
    @SerialName("id") val id: String = UUID.randomUUID().toString(),
    @SerialName("description") val description: String = "",
    @SerialName("timeInSeconds") val addedDateInSeconds: Long = Timestamp.now().seconds,
    @SerialName("author") val author: String = "",
    @SerialName("authorUid") val authorUid: String = "",
    @SerialName("category") val category: String = "",
    val imageRef: String = ""
): java.io.Serializable, Model()
