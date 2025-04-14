package com.example.bai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bai2.ui.theme.Bai2Theme
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Bai2Theme {
                val navController = rememberNavController()
                val taskDatabase = TaskDatabase.getDatabase(applicationContext)
                val taskViewModel: TaskViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return TaskViewModel(taskDatabase.taskDao()) as T
                        }
                    }
                )
                NavHost(navController = navController, startDestination = "task_list") {
                    composable("task_list") {
                        SmartTasksApp(navController = navController, viewModel = taskViewModel)
                    }
                    composable("add_task") {
                        AddTaskScreen(navController = navController, viewModel = taskViewModel)
                    }
                }
            }
        }
    }
}

// Giao diện chính với Scaffold
@Composable
fun SmartTasksApp(navController: NavController, viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
        topBar = { SmartTasksTopBar(navController = navController) },
        content = { padding ->
            TaskStatusScreen(
                tasks = tasks,
                modifier = Modifier.padding(padding).background(Color.White)
            )
        },
        bottomBar = {
            SmartTasksBottomNav(
                onAddClick = { navController.navigate("add_task") { launchSingleTop = true } }
            )
        }
    )
}

// Thanh Top Bar với nút back, tiêu đề và nút thêm
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartTasksTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "List",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF3A86FF)
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    tint = Color(0xFF3A86FF),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("add_task") }) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add New",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF3A86FF), shape = CircleShape)
                        .padding(4.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

// Màn hình thêm task mới
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController, viewModel: TaskViewModel = viewModel()) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add New",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF3A86FF)
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = Color(0xFF3A86FF),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Task",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            )
            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (taskTitle.isEmpty()) {
                                Text(
                                    text = "Do homework",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Text(
                text = "Description",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            )
            Surface(
                modifier = Modifier.fillMaxWidth().height(96.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    BasicTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (taskDescription.isEmpty()) {
                                Text(
                                    text = "Don't give up",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Button(
                onClick = {
                    if (taskTitle.isNotEmpty()) {
                        viewModel.addTask(taskTitle, taskDescription.ifEmpty { "No description" })
                        navController.navigateUp()
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Vui lòng nhập tiêu đề task!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A86FF))
            ) {
                Text(text = "Add", style = TextStyle(fontSize = 16.sp, color = Color.White))
            }
        }
    }
}

// Màn hình hiển thị danh sách task
@Composable
fun TaskStatusScreen(tasks: List<Task>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(task = task)
        }
    }
}

// Giao diện cho từng task
@Composable
fun TaskItem(task: Task) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        color = task.backgroundColor,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.description,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )
        }
    }
}

// Hình dạng notched cho BottomNav
class NotchedShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val cornerRadius = 16.dp.toPx(density)
            val notchRadius = 24.dp.toPx(density)
            val notchCenterX = size.width / 2

            moveTo(0f, cornerRadius)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = cornerRadius * 2,
                    bottom = cornerRadius * 2
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(notchCenterX - notchRadius, 0f)
            arcTo(
                rect = Rect(
                    center = Offset(notchCenterX, 0f),
                    radius = notchRadius
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(size.width - cornerRadius, 0f)
            arcTo(
                rect = Rect(
                    left = size.width - cornerRadius * 2,
                    top = 0f,
                    right = size.width,
                    bottom = cornerRadius * 2
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                rect = Rect(
                    left = size.width - cornerRadius * 2,
                    top = size.height - cornerRadius * 2,
                    right = size.width,
                    bottom = size.height
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cornerRadius, size.height)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - cornerRadius * 2,
                    right = cornerRadius * 2,
                    bottom = size.height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        return Outline.Generic(path)
    }
}

private fun Dp.toPx(density: Density): Float {
    return with(density) { this@toPx.toPx() }
}

// Thanh Bottom Navigation với FAB
@Composable
fun SmartTasksBottomNav(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(NotchedShape())
                .align(Alignment.BottomCenter),
            color = Color.White,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        tint = Color(0xFF3A86FF),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendar",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.file),
                        contentDescription = "Document",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = "Settings",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = Color(0xFF3A86FF),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(48.dp)
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 24.sp
            )
        }
    }
}