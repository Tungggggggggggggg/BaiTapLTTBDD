package com.example.practicalexercise01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicalexercise01.ui.theme.PracticalExercise01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticalExercise01Theme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) { innerPadding ->
                    NavigationSetup(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Điều hướng
@Composable
fun NavigationSetup(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "navigation_screen",
        modifier = modifier
    ) {
        composable("navigation_screen") {
            NavigationScreen(
                onPushClick = { navController.navigate("lazy_column_screen") }
            )
        }
        composable("lazy_column_screen") {
            LazyColumnScreen(
                onBackClick = { navController.popBackStack() },
                onItemClick = { quote, author ->
                    navController.navigate("detail_screen/$quote/$author")
                }
            )
        }
        composable(
            route = "detail_screen/{quote}/{author}",
            arguments = listOf(
                navArgument("quote") { type = androidx.navigation.NavType.StringType },
                navArgument("author") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val quote = backStackEntry.arguments?.getString("quote") ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: ""
            DetailScreen(
                quote = quote,
                author = author,
                onBackClick = { navController.popBackStack() },
                onBackToRootClick = {
                    navController.popBackStack("navigation_screen", inclusive = false)
                }
            )
        }
    }
}

// Màn hình chính
@Composable
fun NavigationScreen(
    onPushClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tiêu đề
        Text(
            text = "Navigation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mô tả
        Text(
            text = "is a framework that simplifies the implementation of navigation between different UI components (activities, fragments, or composables) in an app",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nút "PUSH"
        Button(
            onClick = onPushClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp)
        ) {
            Text(
                text = "PUSH",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationScreenPreview() {
    PracticalExercise01Theme {
        NavigationScreen(onPushClick = {})
    }
}