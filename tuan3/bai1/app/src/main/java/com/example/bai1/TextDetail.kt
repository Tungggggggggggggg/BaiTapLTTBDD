package com.example.bai1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDetailScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Text Detail",
                        color = Color(0xFF007AFF),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Text(
                        text = "<",
                        fontSize = 24.sp,
                        color = Color(0xFF007AFF),
                        modifier = Modifier
                            .clickable { navController.popBackStack() }
                            .padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("The ")

                    withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                        append("quick ")
                    }

                    withStyle(style = SpanStyle(color = Color(0xFFA56629), fontWeight = FontWeight.Bold)) {
                        append("B")
                    }

                    withStyle(style = SpanStyle(color = Color(0xFFA56629))) {
                        append("rown ")
                    }

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append("fox ")
                    }

                    withStyle(style = SpanStyle(letterSpacing = 8.sp)) {
                        append("jumps ")
                    }

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                        append("over ")
                    }

                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("the ")
                    }

                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append("lazy ")
                    }

                    append("dog.")
                },
                fontSize = 40.sp,
                lineHeight = 40.sp,
                letterSpacing = 1.8.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
