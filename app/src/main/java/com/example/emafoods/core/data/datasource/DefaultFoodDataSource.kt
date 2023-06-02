package com.example.emafoods.core.data.datasource

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.data.models.FoodImage
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.network.LogHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultFoodDataSource @Inject constructor(
   private val logHelper: LogHelper
) : FoodDataSource {

    companion object {
        const val FIRESTORE_FOODS_COLLECTION = "ALLFOODS"
        const val FIRESTORE_PENDING_FOODS_COLLECTION = "PENDINGFOODS"
        const val STORAGE_FOODS = "ALLFOODS"
        const val STORAGE_PENDING_FOODS = "PENDINGFOODS"
        const val STORAGE_PENDING_FOODS_TEMPORARY_FOLDER = "TEMPORARY"
        const val FIREBASE_BUCKET_NAME = "emafoods-16e9e.appspot.com"
    }

    private val foodCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_FOODS_COLLECTION)

    private val pendingFoodCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_PENDING_FOODS_COLLECTION)

    private val storage = FirebaseStorage.getInstance()
    private val gsReference =
        storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_FOODS/")
    private val gsPendingReference =
        storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_PENDING_FOODS/")

    override suspend fun addFood(food: Food): State<Food> {
        return try {
            val task = foodCollection.document(food.id).set(food)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not add food to firebase ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not add food to firebase ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun addFoodImageToStorage(
        food: Food,
        fileUri: Uri
    ): State<Food> {
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_FOODS/${food.id}$extension")
            val task = refStorage.putFile(fileUri)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not add food image to storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not add food image to storage ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun addFoodImageBytesToStorage(
        food: Food,
        bytes: ByteArray
    ): State<Food> {
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_FOODS/${food.id}$extension")
            val task = refStorage.putBytes(bytes)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not add food image to storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not add food image to storage ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override fun getAllFoods() = callbackFlow<List<Food>> {
        val snapshotListener = foodCollection.addSnapshotListener { snapshot, error ->
            if (snapshot != null) {
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
        logHelper.reportCrash(it)
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
        logHelper.reportCrash(it)
        emit(listOf())
    }.flowOn(Dispatchers.IO)

    override fun getAllPendingFoods(): Flow<List<Food>> {
        return callbackFlow<List<Food>> {
            val snapshotListener = pendingFoodCollection.addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
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
            logHelper.reportCrash(it)
            emit(emptyList())
        }.flowOn(Dispatchers.IO)
    }

    override fun getAllPendingFoodImages(): Flow<List<FoodImage>> =
        flow<List<FoodImage>> {
            val mutableListOfFoodImages = mutableListOf<FoodImage>()

            val snapshot = gsPendingReference.listAll().await()
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
            logHelper.reportCrash(it)
            emit(listOf())
        }.flowOn(Dispatchers.IO)

    override suspend fun deletePendingFood(food: Food): State<Food> {
        return try {
            val task = pendingFoodCollection.document(food.id).delete()
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not delete food from firebase ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not delete food from firebase ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun deletePendingFoodImage(food: Food): State<Food> {
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/${food.id}$extension")
            val task = refStorage.delete()
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not delete food image from storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not delete food image from storage ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun movePendingImageToAllImages(food: Food): State<Food> {
        // download image from food reference and add it to all images
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/${food.id}$extension")
            val data = refStorage.getBytes(5096 * 5096).await()
            addFoodImageBytesToStorage(food, data)
            deletePendingFoodImage(food)
        } catch (e: Exception) {
            val message = "Could not move pending image to all images ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }


    override suspend fun addPendingFood(food: Food): State<Food> {
        return try {
            val task = pendingFoodCollection.document(food.id).set(food)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not add food to firebase ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not add food to firebase ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun addPendingFoodImageToStorage(
        food: Food,
    ): State<Food> {
        return try {
            val fileUri = Uri.parse(food.imageRef)
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/${food.id}$extension")
            val task = refStorage.putFile(fileUri)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not add pending food image to storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not add pending food image to storage ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun addPendingFoodImageByteArrayToStorage(
        food: Food,
        data: ByteArray
    ): State<Food> {
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/${food.id}$extension")
            val task = refStorage.putBytes(data)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not add pending food image byte array to storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not add pending food image byte array to storage ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun addPendingFoodImageToTemporaryStorage(food: Food): State<Food> {
        return try {
            val fileUri = Uri.parse(food.imageRef)
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/$STORAGE_PENDING_FOODS_TEMPORARY_FOLDER/${food.authorUid}$extension")
            val task = refStorage.putFile(fileUri)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not add pending food image to temporary storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not add pending food image to temporary storage ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    override suspend fun getPendingFoodImageFromTemporaryStorage(authorUid: String): State<Uri> {
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/$STORAGE_PENDING_FOODS_TEMPORARY_FOLDER/$authorUid$extension")
            val task = refStorage.downloadUrl
            task.await()

            if (task.isSuccessful) {
                State.success(task.result)
            } else {
                State.Failed("Could not get food image from temporary storage")
            }
        } catch (e: Exception) {
            State.Failed("Could not get food image from temporary storage")
        }
    }

    override suspend fun moveTemporaryImageToPending(food: Food): State<Food> {
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/$STORAGE_PENDING_FOODS_TEMPORARY_FOLDER/${food.authorUid}$extension")
            val data = refStorage.getBytes(5096 * 5096).await()
            addPendingFoodImageByteArrayToStorage(food, data)
            return deleteTemporaryFoodImage(food)
        } catch (e: Exception) {
            val message = "Could not move temporary image to pending ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }

    private suspend fun deleteTemporaryFoodImage(food: Food): State<Food> {
        return try {
            val extension = ".jpg"
            val refStorage =
                FirebaseStorage.getInstance().reference.child("$STORAGE_PENDING_FOODS/$STORAGE_PENDING_FOODS_TEMPORARY_FOLDER/${food.authorUid}$extension")
            val task = refStorage.delete()
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                State.Failed("Could not delete food image from temporary storage")
            }
        } catch (e: Exception) {
            State.Failed("Could not delete food image from temporary storage")
        }
    }
}