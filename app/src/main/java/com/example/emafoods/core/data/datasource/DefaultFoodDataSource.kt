package com.example.emafoods.core.data.datasource

import android.net.Uri
import com.example.emafoods.core.data.models.Food
import com.example.emafoods.core.data.models.FoodImage
import com.example.emafoods.core.domain.datasource.FoodDataSource
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.addfood.presentation.category.CategoryType
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultFoodDataSource @Inject constructor(
    private val logHelper: LogHelper
) : FoodDataSource {

    companion object {
        const val FIRESTORE_MAIN_DISH_COLLECTION = "MAINDISH"
        const val FIREBASE_DESSERT_COLLECTION = "DESSERT"
        const val FIREBASE_SOUP_COLLECTION = "SOUP"
        const val FIREBASE_BREAKFAST_COLLECTION = "BREAKFAST"
        const val STORAGE_MAIN_DISH = "MAINDISH"
        const val STORAGE_DESSERT = "DESSERT"
        const val STORAGE_SOUP = "SOUP"
        const val STORAGE_BREAKFAST = "BREAKFAST"
        const val FIRESTORE_PENDING_FOODS_COLLECTION = "PENDINGFOODS"
        const val STORAGE_PENDING_FOODS = "PENDINGFOODS"
        const val STORAGE_PENDING_FOODS_TEMPORARY_FOLDER = "TEMPORARY"
        const val FIREBASE_BUCKET_NAME = "emafoods-16e9e.appspot.com"
    }

    private val mainDishCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_MAIN_DISH_COLLECTION)

    private val breakfastCollection = FirebaseFirestore.getInstance()
        .collection(FIREBASE_BREAKFAST_COLLECTION)

    private val dessertCollection = FirebaseFirestore.getInstance()
        .collection(FIREBASE_DESSERT_COLLECTION)

    private val soupCollection = FirebaseFirestore.getInstance()
        .collection(FIREBASE_SOUP_COLLECTION)

    private val pendingFoodCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_PENDING_FOODS_COLLECTION)

    private val storage = FirebaseStorage.getInstance()

    private fun getCollectionFromCategory(categoryType: CategoryType): CollectionReference {
        return when (categoryType) {
            CategoryType.MAIN_DISH -> mainDishCollection
            CategoryType.BREAKFAST -> breakfastCollection
            CategoryType.DESSERT -> dessertCollection
            CategoryType.SOUP -> soupCollection
        }
    }

    private fun getStorageReference(food: Food): StorageReference {
        val extension = ".jpg"
        return when (CategoryType.fromString(food.category)) {
            CategoryType.MAIN_DISH -> FirebaseStorage.getInstance().reference.child("$STORAGE_MAIN_DISH/${food.id}$extension")
            CategoryType.BREAKFAST -> FirebaseStorage.getInstance().reference.child("$STORAGE_BREAKFAST/${food.id}$extension")
            CategoryType.DESSERT -> FirebaseStorage.getInstance().reference.child("$STORAGE_DESSERT/${food.id}$extension")
            CategoryType.SOUP -> FirebaseStorage.getInstance().reference.child("$STORAGE_SOUP/${food.id}$extension")
        }
    }

    override suspend fun addFood(food: Food): State<Food> {
        val collection = getCollectionFromCategory(CategoryType.fromString(food.category))
        return try {
            val task = collection.document(food.id).set(food)
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
            val refStorage = getStorageReference(food)
            refStorage.delete().await()
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
            val refStorage = getStorageReference(food)
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

    override suspend fun getAllFoods(): List<Food> {
        return try {
            val snapshotBreakfast = breakfastCollection.get().await()
            val breakfastList = snapshotBreakfast.toObjects(Food::class.java)
            val snapshotDessert = dessertCollection.get().await()
            val dessertList = snapshotDessert.toObjects(Food::class.java)
            val snapshotMainDish = mainDishCollection.get().await()
            val mainDishList = snapshotMainDish.toObjects(Food::class.java)
            val snapshotSoup = soupCollection.get().await()
            val soupList = snapshotSoup.toObjects(Food::class.java)
            val foods = breakfastList + dessertList + mainDishList + soupList
            foods
        } catch (e: Exception) {
            logHelper.reportCrash(e)
            emptyList<Food>()
        }
    }

    override suspend fun getAllFoodImages(): List<FoodImage> {
        return try {
            val mutableListOfFoodImages = mutableListOf<FoodImage>()
            val snapshotMainDish =
                storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_MAIN_DISH/")
                    .listAll().await()
            val snapshotBreakfast =
                storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_BREAKFAST/")
                    .listAll().await()
            val snapshotDessert =
                storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_DESSERT/")
                    .listAll().await()
            val snapshotSoup =
                storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_SOUP/").listAll()
                    .await()
            val referenceList =
                snapshotMainDish.items + snapshotBreakfast.items + snapshotDessert.items + snapshotSoup.items
            referenceList.forEach { storageReference ->
                mutableListOfFoodImages.add(
                    FoodImage(
                        id = storageReference.name.removeSuffix(".jpg"),
                        imageRef = storageReference.downloadUrl.await().toString()
                    )
                )
            }
            return mutableListOfFoodImages.toList()
        } catch (e: Exception) {
            logHelper.reportCrash(e)
            emptyList<FoodImage>()
        }
    }

    override suspend fun getAllPendingFoods(): List<Food> {
        return try {
            val snapshot = pendingFoodCollection.get().await()
            return snapshot.toObjects(Food::class.java)
        } catch (e: Exception) {
            logHelper.reportCrash(e)
            emptyList<Food>()
        }
    }

    override suspend fun getAllPendingFoodImages(): List<FoodImage> =
        try {
            val mutableListOfFoodImages = mutableListOf<FoodImage>()
            val snapshot =
                storage.getReferenceFromUrl("gs://$FIREBASE_BUCKET_NAME/$STORAGE_PENDING_FOODS/")
                    .listAll().await()
            snapshot.items.forEach { storageReference ->
                mutableListOfFoodImages.add(
                    FoodImage(
                        id = storageReference.name.removeSuffix(".jpg"),
                        imageRef = storageReference.downloadUrl.await().toString()
                    )
                )
            }

            mutableListOfFoodImages.toList()
        } catch (e: Exception) {
            logHelper.reportCrash(e)
            emptyList<FoodImage>()
        }


    override suspend fun deletePendingFood(food: Food): State<Food> {
        return try {
            val task = pendingFoodCollection.document(food.id).delete()
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message =
                    "Could not delete food from firebase ${task.exception?.message}"
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
                val message =
                    "Could not delete food image from storage ${task.exception?.message}"
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
                val message =
                    "Could not add pending food image to storage ${task.exception?.message}"
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
                val message =
                    "Could not add pending food image byte array to storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message =
                "Could not add pending food image byte array to storage ${e.message}"
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
                val message =
                    "Could not add pending food image to temporary storage ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message =
                "Could not add pending food image to temporary storage ${e.message}"
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

    override suspend fun updateFood(food: Food): State<Food> {
        val categoryType = CategoryType.fromString(food.category)
        val collection = getCollectionFromCategory(categoryType)
        return try {
            val task = collection.document(food.id).set(food)
            task.await()
            if (task.isSuccessful) {
                State.success(food)
            } else {
                val message = "Could not update food ${task.exception?.message}"
                logHelper.reportCrash(Throwable(message))
                State.Failed(message)
            }
        } catch (e: Exception) {
            val message = "Could not update food ${e.message}"
            logHelper.reportCrash(Throwable(message))
            State.Failed(message)
        }
    }
}