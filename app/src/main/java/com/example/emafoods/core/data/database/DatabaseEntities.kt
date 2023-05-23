package com.example.emafoods.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.emafoods.core.data.models.Food
import com.google.firebase.Timestamp

@Entity
data class DatabaseFood (
    @PrimaryKey
    val id: String = "",
    val author: String = "",
    val authorUid: String = "",
    val description: String = "",
    val addedDateInSeconds: Long = Timestamp.now().seconds,
    val imageRef: String = ""
)

@Entity
data class DatabasePendingFood (
    @PrimaryKey
    val id: String = "",
    val author: String = "",
    val authorUid: String = "",
    val description: String = "",
    val addedDateInSeconds: Long = Timestamp.now().seconds,
    val imageRef: String = ""
)

fun List<DatabaseFood>.asDomainModel(): List<Food> {
    return map {
        Food(
            id = it.id,
            author = it.author,
            authorUid = it.authorUid,
            description = it.description,
            addedDateInSeconds = it.addedDateInSeconds,
            imageRef = it.imageRef
        )
    }
}

fun List<DatabasePendingFood>.asDomainPendingModel(): List<Food> {
    return map {
        Food(
            id = it.id,
            author = it.author,
            authorUid = it.authorUid,
            description = it.description,
            addedDateInSeconds = it.addedDateInSeconds,
            imageRef = it.imageRef
        )
    }
}

fun List<Food>.asDatabaseModel(): List<DatabaseFood> {
    return this.map {
        DatabaseFood(
            id = it.id,
            author = it.author,
            authorUid = it.authorUid,
            description = it.description,
            addedDateInSeconds = it.addedDateInSeconds,
            imageRef = it.imageRef
        )
    }
}

fun List<Food>.asDatabasePendingModel(): List<DatabasePendingFood> {
    return this.map {
        DatabasePendingFood(
            id = it.id,
            author = it.author,
            authorUid = it.authorUid,
            description = it.description,
            addedDateInSeconds = it.addedDateInSeconds,
            imageRef = it.imageRef
        )
    }
}