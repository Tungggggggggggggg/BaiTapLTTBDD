package com.example.bai1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bai1.ui.theme.Bai1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bai1Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        JetpackComposeScreen(navController = navController)
                    }
                    composable("uiComponentsList") {
                        UIComponentsList(navController = navController)
                    }
                    composable("textDetail") {
                        TextDetailScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun JetpackComposeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = "Jetpack Compose",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 100.dp, bottom = 8.dp)
        )

        Text(
            text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 200.dp)
        )

        Button(
            onClick = {
                navController.navigate("uiComponentsList")
            },
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 8.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF00A6FB))
        ) {
            Text(
                text = "I’m ready",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Bai1Theme {
        JetpackComposeScreen(navController = rememberNavController())
    }
}
