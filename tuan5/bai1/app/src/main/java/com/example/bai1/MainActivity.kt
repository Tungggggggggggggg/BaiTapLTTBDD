package com.example.bai1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bai1.ui.theme.Bai1Theme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Khởi tạo Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Cấu hình Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Thiết lập giao diện
        setContent {
            Bai1Theme {
                var message by remember { mutableStateOf<String?>(null) }
                var messageColor by remember { mutableStateOf<Color?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        onLoginClick = { signIn { newMessage, newColor ->
                            message = newMessage
                            messageColor = newColor
                        } },
                        message = message,
                        messageColor = messageColor,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Đăng ký Activity Result để nhận kết quả đăng nhập
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Đăng nhập thất bại (người dùng hủy)
            setContent {
                Bai1Theme {
                    var message by remember { mutableStateOf<String?>(null) }
                    var messageColor by remember { mutableStateOf<Color?>(null) }

                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        LoginScreen(
                            onLoginClick = { signIn { newMessage, newColor ->
                                message = newMessage
                                messageColor = newColor
                            } },
                            message = "Google Sign-In Failed\nUser cancelled the Google sign-in process.",
                            messageColor = Color(0xFFFFCDD2),
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    // Bắt đầu quá trình đăng nhập
    private fun signIn(updateMessage: (String?, Color?) -> Unit) {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    // Xác thực với Firebase bằng Google ID Token
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Đăng nhập thành công
                    val user = auth.currentUser
                    setContent {
                        Bai1Theme {
                            var message by remember { mutableStateOf<String?>(null) }
                            var messageColor by remember { mutableStateOf<Color?>(null) }

                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                LoginScreen(
                                    onLoginClick = { signIn { newMessage, newColor ->
                                        message = newMessage
                                        messageColor = newColor
                                    } },
                                    message = "Success!\nHi ${user?.email}\nWelcome to UTHSmartTasks",
                                    messageColor = Color(0xFFBBDEFB),
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                } else {
                    // Đăng nhập thất bại
                    setContent {
                        Bai1Theme {
                            var message by remember { mutableStateOf<String?>(null) }
                            var messageColor by remember { mutableStateOf<Color?>(null) }

                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                LoginScreen(
                                    onLoginClick = { signIn { newMessage, newColor ->
                                        message = newMessage
                                        messageColor = newColor
                                    } },
                                    message = "Google Sign-In Failed\nError: ${task.exception?.message}",
                                    messageColor = Color(0xFFFFCDD2),
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
    }
}

// Composable cho giao diện đăng nhập
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    message: String? = null,
    messageColor: Color? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF42A5F5)
            ),
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 15.dp)
        ) {
            Text(
                text = "Login by Gmail",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        if (message != null && messageColor != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(messageColor)
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}

// Preview cho giao diện ban đầu
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Bai1Theme {
        LoginScreen(
            onLoginClick = {},
            message = null,
            messageColor = null
        )
    }
}

// Preview cho trạng thái đăng nhập thất bại
@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    Bai1Theme {
        LoginScreen(
            onLoginClick = {},
            message = "Google Sign-In Failed\nUser cancelled the Google sign-in process.",
            messageColor = Color(0xFFFFCDD2)
        )
    }
}

// Preview cho trạng thái đăng nhập thành công
@Preview(showBackground = true)
@Composable
fun LoginScreenSuccessPreview() {
    Bai1Theme {
        LoginScreen(
            onLoginClick = {},
            message = "Success!\nHi sample@gmail.com\nWelcome to UTHSmartTasks",
            messageColor = Color(0xFFBBDEFB)
        )
    }
}