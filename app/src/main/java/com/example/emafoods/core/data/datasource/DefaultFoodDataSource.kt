package com.example.emafoods.core.data.datasource

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.data.models.FoodImage
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.utils.State
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class DefaultFoodDataSource: FoodDataSource {

    companion object {
        const val FIRESTORE_FOODS_COLLECTION = "ALLFOODS"
        const val STORAGE_FOODS = "ALLFOODS"
        const val FIREBASE_BUCKET_NAME = "emafoods-16e9e.appspot.com"
    }

    private val foodCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_FOODS_COLLECTION)

    private val storage = FirebaseStorage.getInstance()
    private val gsReference = storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_FOODS/")

    override suspend fun addFood(food: Food): State<Food> {
        val task = foodCollection.document(food.id).set(food)
        task.await()
        return if(task.isSuccessful) {
            State.success(food)
        } else {
            State.Failed("Could not add food to firebase")
        }
    }

    override suspend fun addFoodImageToStorage(food: Food, fileUri: Uri): State<Food> {
        val extension = ".jpg"
        val refStorage =
            FirebaseStorage.getInstance().reference.child("$STORAGE_FOODS/${food.id}$extension")
        val task = refStorage.putFile(fileUri)
        task.await()
        return if (task.isSuccessful) {
            State.success(food)
        } else {
            State.Failed("Could not add food image to storage")
        }
    }

    override fun getAllFoods() = callbackFlow<List<Food>>{
        val snapshotListener = foodCollection.addSnapshotListener { snapshot, error ->
            if(snapshot != null) {
                val foods = snapshot.toObjects(Food::class.java)
                trySend(foods)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose {
            snapshotListener.remove()
        }
    }.catch {
        emit(emptyList())
    }.flowOn(Dispatchers.IO)


    override fun getAllFoodImages() = flow<List<FoodImage>> {
        val mutableListOfFoodImages = mutableListOf<FoodImage>()

        val snapshot = gsReference.listAll().await()
        snapshot.items.forEach { storageReference ->
            mutableListOfFoodImages.add(
                FoodImage(
                    id = storageReference.name.removeSuffix(".jpg"),
                    imageRef = storageReference.downloadUrl.await().toString()
                )
            )
        }

        emit(mutableListOfFoodImages.toList())
    }.catch {
        emit(listOf())
    }.flowOn(Dispatchers.IO)
}