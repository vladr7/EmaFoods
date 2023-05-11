package com.example.emafoods.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.emafoods.core.data.models.Food
import com.google.firebase.Timestamp

@Entity
data class DatabaseFood (
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val addedDateInSeconds: Long = Timestamp.now().seconds,
    val imageRef: String = ""
)

fun List<DatabaseFood>.asDomainModel(): List<Food> {
    return map {
        Food(
            id = it.id,
            description = it.description,
            addedDateInSeconds = it.addedDateInSeconds,
            imageRef = it.imageRef
        )
    }
}

fun DatabaseFood.asDomainModel(): Food {
    return Food(
        id = this.id,
        description = this.description,
        addedDateInSeconds = this.addedDateInSeconds,
        imageRef = this.imageRef
    )
}

fun List<Food>.asDatabaseModel(): List<DatabaseFood> {
    return this.map {
        DatabaseFood(
            id = it.id,
            description = it.description,
            addedDateInSeconds = it.addedDateInSeconds,
            imageRef = it.imageRef
        )
    }
}

fun Food.asDatabaseModel() : DatabaseFood {
    return DatabaseFood(
        id = this.id,
        description = this.description,
        addedDateInSeconds = this.addedDateInSeconds,
        imageRef = this.imageRef

    )
}

