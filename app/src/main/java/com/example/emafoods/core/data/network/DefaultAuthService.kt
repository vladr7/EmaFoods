package com.example.emafoods.core.data.network

import android.util.Log
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultAuthService @Inject constructor() : AuthService {

    companion object {
        const val FIRESTORE_USERS_COLLECTION = "USERS"
        const val FIRESTORE_USER_AWAITING_REWARDS = "awaitingRewards"
    }

    private val usersCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_USERS_COLLECTION)
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun isUserSignedIn(): Boolean =
        firebaseAuth.currentUser != null

    override suspend fun getUserDetails(): UserData {
        val uid = firebaseAuth.currentUser?.uid
        return try {
            val documentSnapshot = usersCollection.document(uid ?: "").get().await()
            val userData = documentSnapshot.toObject(UserData::class.java)
            userData ?: UserData(
                uid = firebaseAuth.currentUser?.uid ?: "",
                email = firebaseAuth.currentUser?.email ?: "",
                displayName = firebaseAuth.currentUser?.displayName ?: "",
            )
        } catch (e: Exception) {
            UserData(
                uid = firebaseAuth.currentUser?.uid ?: "",
                email = firebaseAuth.currentUser?.email ?: "",
                displayName = firebaseAuth.currentUser?.displayName ?: "",
            )
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun signIn(idToken: String): State<Unit> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        continuation.resume(State.success(Unit))
                    } else {
                        continuation.resume(State.failed(task.exception?.localizedMessage ?: ""))
                    }
                }
        }
    }

    override suspend fun addUserDataToFirestore(userData: UserData) {
        val uid = firebaseAuth.currentUser?.uid
        usersCollection.document(uid ?: userData.email).set(userData)
    }

    override suspend fun addRewardToUser(rewardedUserUid: String) {
        val awaitingRewards = usersCollection.document(rewardedUserUid).get().await().get(
            FIRESTORE_USER_AWAITING_REWARDS
        )
        try {
            val currentRewards = if (awaitingRewards != null) awaitingRewards as Long else 0L
            usersCollection.document(rewardedUserUid)
                .update(FIRESTORE_USER_AWAITING_REWARDS, currentRewards + 1)
        } catch (e: Exception) {
            Log.d("DefaultAuthService", "addRewardToUser: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserRewards(): State<Long> {
        val uid = firebaseAuth.currentUser?.uid
        return try {
            val awaitingRewards = usersCollection.document(uid ?: "").get().await().get(
                FIRESTORE_USER_AWAITING_REWARDS
            )
            val currentRewards = if (awaitingRewards != null) awaitingRewards as Long else 0L
            State.success(currentRewards)
        } catch (e: Exception) {
            State.failed(e.localizedMessage ?: "")
        }
    }

    override suspend fun resetUserRewards() {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update(FIRESTORE_USER_AWAITING_REWARDS, 0L)
        } catch (e: Exception) {
            Log.d("DefaultAuthService", "resetUserRewards: ${e.localizedMessage}")
        }
    }

    override suspend fun storeUserXP(xpToBeStored: Long) {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update("userXp", xpToBeStored)
        } catch (e: Exception) {
            Log.d("DefaultAuthService", "storeUserXP: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserXP(): Long {
        val uid = firebaseAuth.currentUser?.uid
        return try {
            val userXp = usersCollection.document(uid ?: "").get().await().get("userXp")
            if (userXp != null) userXp as Long else 0L
        } catch (e: Exception) {
            Log.d("DefaultAuthService", "getUserXP: ${e.localizedMessage}")
            0
        }
    }

    override suspend fun storeUserLevel(userLevel: UserLevel) {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update("userLevel", userLevel.string)
        } catch (e: Exception) {
            Log.d("DefaultAuthService", "storeUserLevel: ${e.localizedMessage}")
        }
    }

    override suspend fun upgradeBasicUserToAdmin() {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update("admin", true)
        } catch (e: Exception) {
            Log.d("DefaultAuthService", "upgradeBasicUserToAdmin: ${e.localizedMessage}")
        }
    }
}