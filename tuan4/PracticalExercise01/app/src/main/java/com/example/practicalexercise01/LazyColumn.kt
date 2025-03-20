package com.example.practicalexercise01

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicalexercise01.ui.theme.PracticalExercise01Theme

// Danh sách trích dẫn
object QuoteData {
    data class Quote(val text: String, val author: String)

    val quotes = listOf(
        Quote("To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment.", "Ralph Waldo Emerson"),
        Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
        Quote("In the middle of difficulty lies opportunity.", "Albert Einstein"),
        Quote("Life is what happens when you're busy making other plans.", "John Lennon"),
        Quote("The best way to predict the future is to create it.", "Peter Drucker"),
        Quote("You must be the change you wish to see in the world.", "Mahatma Gandhi"),
        Quote("Success is not final, failure is not fatal: It is the courage to continue that counts.", "Winston Churchill"),
        Quote("I have a dream that one day this nation will rise up and live out the true meaning of its creed.", "Martin Luther King Jr."),
        Quote("Imagination is more important than knowledge. For knowledge is limited, whereas imagination embraces the entire world.", "Albert Einstein"),
        Quote("Do not go where the path may lead, go instead where there is no path and leave a trail.", "Ralph Waldo Emerson")
    )

    val repeatedQuotes: List<Quote> = List(1_000) { index ->
        quotes[index % quotes.size]
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyColumnScreen(
    onBackClick: () -> Unit,
    onItemClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "LazyColumn",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3A86FF),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    // Nút back
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { onBackClick() }
                            .padding(start = 20.dp, end = 8.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF3A86FF))
                    )
                },
                actions = {
                    Spacer(modifier = Modifier.size(40.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
            )
        }
    ) { innerPadding ->
        // Danh sách trích dẫn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(QuoteData.repeatedQuotes) { index, quote ->
                LazyColumnItem(
                    index = index + 1,
                    content = quote.text,
                    author = quote.author,
                    onItemClick = { onItemClick(quote.text, quote.author) }
                )
            }
        }
    }
}

// Item trong danh sách
@Composable
fun LazyColumnItem(
    index: Int,
    content: String,
    author: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE6F0FA),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onItemClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Số thứ tự
        Text(
            text = "$index) ",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Nội dung trích dẫn
        Text(
            text = content,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )

        // Nút ">"
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .rotate(180f)
                .clickable { onItemClick() },
            colorFilter = ColorFilter.tint(Color(0xFF3A86FF))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LazyColumnScreenPreview() {
    PracticalExercise01Theme {
        LazyColumnScreen(
            onBackClick = {},
            onItemClick = { _, _ -> }
        )
    }
}