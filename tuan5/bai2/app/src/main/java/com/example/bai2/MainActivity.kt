package com.example.bai2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bai2.ui.theme.Bai2Theme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Kiểm tra Google Play Services
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 9000)?.show()
            } else {
                Toast.makeText(this, "Google Play Services không khả dụng trên thiết bị này", Toast.LENGTH_LONG).show()
            }
            return
        }

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("796226921151-tgki2u96ersiv8sr3mrfm0a25vbhlo5g.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            Bai2Theme {
                var userName by remember { mutableStateOf("") }
                var userEmail by remember { mutableStateOf("") }
                var userDob by remember { mutableStateOf("") }
                var userPhotoUrl by remember { mutableStateOf<String?>(null) }

                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        userName = currentUser.displayName ?: "Unknown"
                        userEmail = currentUser.email ?: "Unknown"
                        userDob = "16/12/2004"
                        userPhotoUrl = currentUser.photoUrl?.toString()
                        navController.navigate("profile") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onSignInClick = {
                                val signInIntent = googleSignInClient.signInIntent
                                signInLauncher.launch(signInIntent)
                            },
                            modifier = Modifier
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            userName = userName,
                            userEmail = userEmail,
                            userDob = userDob,
                            photoUrl = userPhotoUrl,
                            onBackClick = {
                                auth.signOut()
                                googleSignInClient.signOut()
                                navController.navigate("login") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                        )
                    }
                }

                HandleSignInResult(
                    navController = navController,
                    onSignInSuccess = { name, email, dob, photoUrl ->
                        userName = name
                        userEmail = email
                        userDob = dob
                        userPhotoUrl = photoUrl
                    },
                    onSignInError = { errorMessage ->
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!, account.photoUrl?.toString())
        } catch (e: ApiException) {
            signInResult = SignInResult.Error("Google Sign-In failed: ${e.statusCode} - ${e.message}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, photoUrl: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        signInResult = SignInResult.Success(
                            name = it.displayName ?: "Unknown",
                            email = it.email ?: "Unknown",
                            dob = "16/12/2004",
                            photoUrl = photoUrl ?: user.photoUrl?.toString()
                        )
                    }
                } else {
                    signInResult = SignInResult.Error("Firebase Auth failed: ${task.exception?.message}")
                }
            }
    }
}

sealed class SignInResult {
    data class Success(val name: String, val email: String, val dob: String, val photoUrl: String?) : SignInResult() // Thêm photoUrl
    data class Error(val message: String) : SignInResult()
    object Idle : SignInResult()
}

private var signInResult: SignInResult by mutableStateOf(SignInResult.Idle)

@Composable
fun HandleSignInResult(
    navController: NavController,
    onSignInSuccess: (String, String, String, String?) -> Unit,
    onSignInError: (String) -> Unit
) {
    LaunchedEffect(signInResult) {
        when (val result = signInResult) {
            is SignInResult.Success -> {
                onSignInSuccess(result.name, result.email, result.dob, result.photoUrl)
                navController.navigate("profile") {
                    popUpTo("login") { inclusive = true }
                }
                signInResult = SignInResult.Idle
            }
            is SignInResult.Error -> {
                onSignInError(result.message)
                signInResult = SignInResult.Idle
            }
            is SignInResult.Idle -> {
            }
        }
    }
}