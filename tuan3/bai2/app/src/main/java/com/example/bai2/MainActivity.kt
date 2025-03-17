package com.example.bai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bai2.ui.theme.Bai2Theme
import androidx.compose.foundation.clickable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Bai2Theme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "main", modifier = Modifier.padding(innerPadding)) {
                        composable("main") {
                            MainScreen(navController = navController)
                        }
                        composable("easyTime") {
                            EasyTimeScreen(navController = navController)
                        }
                        composable("increaseWork") {
                            IncreaseWorkScreen(navController = navController)
                        }
                        composable("reminder") {
                            ReminderScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)
                .clickable {
                    navController.navigate("easyTime")
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "UTH SmartTasks",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007AFF),
                modifier = Modifier.padding(bottom = 4.dp)
                    .clickable {
                        navController.navigate("easyTime")
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Bai2Theme {
        MainScreen(navController = rememberNavController())
    }
}
