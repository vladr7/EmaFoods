package com.example.emafoods.core.data.network

import android.util.Log
import com.example.emafoods.core.domain.models.State
import com.example.emafoods.core.domain.models.UserData
import com.example.emafoods.core.domain.network.AuthService
import com.example.emafoods.core.domain.network.LogHelper
import com.example.emafoods.feature.game.domain.model.UserLevel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultAuthService @Inject constructor(
    private val logHelper: LogHelper
) : AuthService {

    companion object {
        const val FIRESTORE_USERS_COLLECTION = "USERS"
        const val FIRESTORE_USER_AWAITING_REWARDS = "awaitingRewards"
        const val FIRESTORE_SETTINGS_COLLECTION = "SETTINGS"
        const val FIRESTORE_SETTINGS_DOC_ID = "e1CRFkuAb4iLCWSI8aIa"
    }

    private val usersCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_USERS_COLLECTION)
    private val settingsCollection = FirebaseFirestore.getInstance()
        .collection(FIRESTORE_SETTINGS_COLLECTION)
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun isUserSignedIn(): Boolean =
        firebaseAuth.currentUser != null

    override suspend fun getUserDetails(): UserData {
        val uid = firebaseAuth.currentUser?.uid
        return try {
            val documentSnapshot = usersCollection.document(uid ?: "").get().await()
            val userData = documentSnapshot.toObject(UserData::class.java)
            if (userData != null) {
                userData
            } else {
                logHelper.reportCrash(Throwable("DefaultAuthService: GetUserDetails: User data is null"))
                UserData(
                    uid = firebaseAuth.currentUser?.uid ?: "",
                    email = firebaseAuth.currentUser?.email ?: "",
                    displayName = firebaseAuth.currentUser?.displayName ?: "User${uid?.substring(0, 5)}",
                )
            }
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: GetUserDetails: ${e.localizedMessage}"))
            UserData(
                uid = firebaseAuth.currentUser?.uid ?: "",
                email = firebaseAuth.currentUser?.email ?: "",
                displayName = firebaseAuth.currentUser?.displayName ?: "User${uid?.substring(0, 5)}",
            )
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun signInGoogle(idToken: String): State<Unit> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        continuation.resume(State.success(Unit))
                    } else {
                        logHelper.reportCrash(Throwable("DefaultAuthService: SignIn: ${task.exception?.localizedMessage}"))
                        continuation.resume(State.failed(task.exception?.localizedMessage ?: ""))
                    }
                }
        }
    }

    override suspend fun signInAnonymous(): State<Unit> {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInAnonymously()
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        continuation.resume(State.success(Unit))
                    } else {
                        logHelper.reportCrash(Throwable("DefaultAuthService: SignIn: ${task.exception?.localizedMessage}"))
                        continuation.resume(State.failed(task.exception?.localizedMessage ?: ""))
                    }
                }
        }
    }

    override suspend fun addUserDataToFirestore(userData: UserData) {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: userData.email).set(userData)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: addUserDataToFirestore: ${e.localizedMessage}"))
        }
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
            logHelper.reportCrash(Throwable("DefaultAuthService: getUserRewards: ${e.localizedMessage}"))
            State.failed(e.localizedMessage ?: "")
        }
    }

    override suspend fun resetUserRewards() {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update(FIRESTORE_USER_AWAITING_REWARDS, 0L)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: resetUserRewards: ${e.localizedMessage}"))
        }
    }

    override suspend fun storeUserXP(xpToBeStored: Long) {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update("userXp", xpToBeStored)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: storeUserXP: ${e.localizedMessage}"))
        }
    }

    override suspend fun getUserXP(): Long {
        val uid = firebaseAuth.currentUser?.uid
        return try {
            val userXp = usersCollection.document(uid ?: "").get().await().get("userXp")
            if (userXp != null) userXp as Long else 0L
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: getUserXP: ${e.localizedMessage}"))
            0
        }
    }

    override suspend fun storeUserLevel(userLevel: UserLevel) {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update("userLevel", userLevel.string)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: storeUserLevel: ${e.localizedMessage}"))
        }
    }

    override suspend fun upgradeBasicUserToAdmin() {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update("admin", true)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: upgradeBasicUserToAdmin: ${e.localizedMessage}"))
        }
    }

    override suspend fun updateUserName(userName: String) {
        val uid = firebaseAuth.currentUser?.uid
        try {
            usersCollection.document(uid ?: "")
                .update("displayName", userName)
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: updateUserName: ${e.localizedMessage}"))
        }
    }

    override suspend fun getAdminCode(): String {
        return try {
            val adminCode = settingsCollection.document(FIRESTORE_SETTINGS_DOC_ID).get().await().get("adminCode")
            if (adminCode != null) adminCode as String else ""
        } catch (e: Exception) {
            logHelper.reportCrash(Throwable("DefaultAuthService: getAdminCode: ${e.localizedMessage}"))
            ""
        }
    }
}