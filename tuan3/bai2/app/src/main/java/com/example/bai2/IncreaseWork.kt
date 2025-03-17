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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bai2.ui.theme.Bai2Theme
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape

class IncreaseWorkActivity : ComponentActivity() {
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
                    IncreaseWorkScreen(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IncreaseWorkScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007AFF),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable {
                        navController.navigate("easyTime")
                    }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .border(1.dp, Color.Black, shape = CircleShape)
                        .padding(4.dp)
                )

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .border(0.dp, Color.Black, shape = CircleShape)
                        .background(Color(0xFF00A6FB), shape = CircleShape)
                        .padding(4.dp)
                )

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .border(1.dp, Color.Black, shape = CircleShape)
                        .padding(4.dp)
                )
            }

            Text(
                text = "Skip",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007AFF),
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable {
                        navController.navigate("main")
                    }
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.anh3),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Increase Work Effectiveness",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Time management and the determination of more important tasks will give your job statistics better and always improve.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = { navController.navigate("reminder") },
            modifier = Modifier
                .width(250.dp)
                .padding(top = 32.dp, bottom = 120.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A6FB))
        ) {
            Text("Next", color = Color.White, fontSize = 16.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun IncreaseWorkPreview() {
    Bai2Theme {
        IncreaseWorkScreen(navController = rememberNavController())
    }
}
