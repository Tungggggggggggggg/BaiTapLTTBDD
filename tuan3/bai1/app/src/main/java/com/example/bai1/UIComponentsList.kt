package com.example.bai1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable


@Composable
fun UIComponentsList(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "UI Components List",
            fontSize = 20.sp,
            color = Color(0xFF007AFF),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .align(Alignment.CenterHorizontally)
        )

        SectionHeader(title = "Display")
        ComponentItem(name1 = "Text", name2 = "Displays text", navController = navController)
        ComponentItem(name1 = "Image", name2 = "Displays an image", navController = navController)

        Spacer(modifier = Modifier.height(8.dp))

        SectionHeader(title = "Input")
        ComponentItem(name1 = "TextField", name2 = "Input field for text", navController = navController)
        ComponentItem(name1 = "PasswordField", name2 = "Input field for passwords", navController = navController)

        Spacer(modifier = Modifier.height(8.dp))

        SectionHeader(title = "Layout")
        ComponentItem(name1 = "Column", name2 = "Arranges elements vertically", navController = navController)
        ComponentItem(name1 = "Row", name2 = "Arranges elements horizontally", navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {  navController.popBackStack()  },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF00A6FB))
        ) {
            Text("Go Back", color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 12.dp)
    )
}

@Composable
fun ComponentItem(name1: String, name2: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                if (name1 == "Text") {
                    navController.navigate("textDetail")
                }
                if (name1 == "Image") {
                    navController.navigate("imageDetail")
                }
                if (name1 == "TextField") {
                    navController.navigate("textfieldDetail")
                }
                if (name1 == "PasswordField") {
                    navController.navigate("passwordDetail")
                }
                if (name1 == "Column") {
                    navController.navigate("columnDetail")
                }
                if (name1 == "Row") {
                    navController.navigate("rowDetail")
                }
            },
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF90E0EF)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Text(
                text = name1,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = name2,
                fontSize = 14.sp,
                color = Color.Black,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUIComponentsList() {
    val fakeNavController = rememberNavController()
    UIComponentsList(navController = fakeNavController)
}
