package com.example.practicalexercise02

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.*

// MainActivity: Điểm khởi đầu của ứng dụng, thiết lập navigation và giao diện chính
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTasksTheme {
                val navController = rememberNavController()
                // NavHost: Điều hướng giữa các màn hình (Task List và Task Detail)
                NavHost(navController = navController, startDestination = "task_list") {
                    composable("task_list") {
                        SmartTasksApp(navController)
                    }
                    composable(
                        route = "task_detail/{taskId}",
                        arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getInt("taskId")
                        val viewModel: TaskViewModel = viewModel()
                        val task = taskId?.let { viewModel.getTaskById(it) }
                        if (task != null) {
                            DetailScreen(navController = navController, task = task)
                        } else {
                            Text("Task not found", modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}

// SmartTasksTheme: Theme chính của ứng dụng, định nghĩa màu sắc và typography
@Composable
fun SmartTasksTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        background = Color.White,
        surface = Color.White,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}

// SmartTasksApp: Giao diện chính của ứng dụng với TopBar, nội dung và BottomNav
@Composable
fun SmartTasksApp(navController: NavController) {
    Scaffold(
        topBar = { SmartTasksTopBar() },
        content = { padding ->
            TaskStatusScreen(
                modifier = Modifier.padding(padding),
                navController = navController
            )
        },
        bottomBar = { SmartTasksBottomNav() }
    )
}

// SmartTasksTopBar: Thanh trên cùng hiển thị tiêu đề, logo và nút thông báo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartTasksTopBar() {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SmartTasks",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color(0xFF3A86FF)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A simple and efficient to-do app",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF3A86FF)
                    )
                )
            }
        },
        navigationIcon = {
            // Logo của ứng dụng
            Image(
                painter = painterResource(id = R.drawable.logouth),
                contentDescription = "UTH Logo",
                modifier = Modifier
                    .size(80.dp)
                    .padding(start = 16.dp, top = 30.dp, end = 8.dp)
            )
        },
        actions = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Nút thông báo với badge đỏ
                IconButton(onClick = { }) {
                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.bell),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(30.dp),
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color.Red, shape = CircleShape)
                                .align(Alignment.TopEnd)
                                .offset(x = (-2).dp, y = 2.dp)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF3A86FF),
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    )
}

// ApiResponse: Data class để parse JSON từ API
data class ApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<TaskData>,
    val reminders: List<Reminder>?
)

// TaskData: Data class để parse dữ liệu task từ API
@kotlinx.serialization.Serializable
data class TaskData(
    val id: Int,
    val title: String?,
    val description: String?,
    val status: String?,
    val priority: String?,
    val category: String?,
    val dueDate: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val subtasks: List<Subtask>?,
    val fileName: String?,
    val fileUrl: String?
)

// Subtask: Data class cho subtask của một task
@kotlinx.serialization.Serializable
data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

// Reminder: Data class cho thông tin nhắc nhở
data class Reminder(
    val id: Int,
    val time: String,
    val type: String
)

// Task: Data class để hiển thị task trong giao diện
@kotlinx.serialization.Serializable
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val time: String,
    val completed: Boolean,
    val priority: String,
    val category: String,
    val subtasks: List<Subtask>,
    val fileName: String?,
    val fileUrl: String?
)

// TaskApiService: Interface để gọi API lấy danh sách task
interface TaskApiService {
    @GET("researchUTH/tasks")
    suspend fun getTasks(): ApiResponse
}

// RetrofitClient: Khởi tạo Retrofit để gọi API
object RetrofitClient {
    private const val BASE_URL = "https://amock.io/api/"

    val instance: TaskApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(TaskApiService::class.java)
    }
}

// TaskViewModel: Quản lý dữ liệu task, gọi API và cung cấp dữ liệu cho giao diện
class TaskViewModel : ViewModel() {
    var tasks by mutableStateOf<List<Task>>(emptyList())
        private set

    init {
        fetchTasks()
    }

    // fetchTasks: Gọi API để lấy danh sách task
    private fun fetchTasks() {
        viewModelScope.launch {
            try {
                Log.d("TaskViewModel", "Fetching tasks from API...")
                val response = RetrofitClient.instance.getTasks()
                Log.d("TaskViewModel", "API Response: $response")
                if (response.isSuccess) {
                    tasks = response.data
                        .filter { it.title != null }
                        .map { taskData ->
                            val isCompleted = taskData.subtasks?.all { it.isCompleted } ?: false
                            Task(
                                id = taskData.id,
                                title = taskData.title!!,
                                description = taskData.description ?: "No description",
                                status = taskData.status ?: "Unknown",
                                time = taskData.dueDate?.let { formatDate(it) } ?: "N/A",
                                completed = isCompleted,
                                priority = taskData.priority ?: "Unknown",
                                category = taskData.category ?: "Unknown",
                                subtasks = taskData.subtasks ?: emptyList(),
                                fileName = taskData.fileName,
                                fileUrl = taskData.fileUrl
                            )
                        }
                    Log.d("TaskViewModel", "Parsed tasks: $tasks")
                } else {
                    Log.d("TaskViewModel", "API call failed: ${response.message}")
                    tasks = emptyList()
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error fetching tasks: ${e.message}", e)
                tasks = emptyList()
            }
        }
    }

    // getTaskById: Lấy task theo ID
    fun getTaskById(id: Int): Task? {
        return tasks.find { it.id == id }
    }

    // formatDate: Chuyển đổi định dạng ngày từ ISO sang định dạng hiển thị
    private fun formatDate(isoDate: String): String {
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = isoFormat.parse(isoDate)
            val outputFormat = SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error formatting date: ${e.message}", e)
            "N/A"
        }
    }
}

// TaskStatusScreen: Màn hình hiển thị danh sách task hoặc thông báo không có task
@Composable
fun TaskStatusScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel(),
    navController: NavController
) {
    val tasks by remember { derivedStateOf { viewModel.tasks } }

    if (tasks.isEmpty()) {
        // Hiển thị khi không có task
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "No Tasks Icon",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Tasks Yet!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Stay productive—add something to do",
                style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
            )
        }
    } else {
        // Hiển thị danh sách task
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                TaskItem(task = task, onClick = {
                    navController.navigate("task_detail/${task.id}")
                })
            }
        }
    }
}

// TaskItem: Thành phần hiển thị một task trong danh sách
@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    var isCompleted by remember { mutableStateOf(task.completed) }

    val backgroundColor = when (task.status) {
        "In Progress" -> Color(0xFFFFE6E6)
        "Pending" -> Color(0xFFE6F0FA)
        else -> Color(0xFFE6F0FA)
    }
    val statusColor = when (task.status) {
        "In Progress" -> Color(0xFF4CAF50)
        "Pending" -> Color(0xFFF44336)
        else -> Color.Black
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = backgroundColor,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox để đánh dấu task hoàn thành
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { newValue ->
                    isCompleted = newValue
                },
                modifier = Modifier.size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF3A86FF),
                    uncheckedColor = Color(0xFF3A86FF),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Thông tin task: tiêu đề, mô tả, trạng thái và thời gian
            Column {
                Text(
                    text = task.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    style = TextStyle(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Status: ${task.status}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = statusColor
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.time,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
        }
    }
}

// NotchedShape: Shape tùy chỉnh cho BottomNav với phần notch ở giữa
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

// toPx: Chuyển đổi Dp sang pixel
private fun Dp.toPx(density: Density): Float {
    return with(density) { this@toPx.toPx() }
}

// SmartTasksBottomNav: Thanh điều hướng dưới cùng với các nút và FAB
@Composable
fun SmartTasksBottomNav() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        // Thanh điều hướng với các nút
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(NotchedShape())
                .align(Alignment.BottomCenter),
            color = Color(0xFFF5F5F5),
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                // Nút Home
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        tint = Color(0xFF3A86FF),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Nút Calendar
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendar",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                // Nút Document
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.file),
                        contentDescription = "Document",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Nút Settings
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
        // Floating Action Button: Nút thêm task mới
        FloatingActionButton(
            onClick = { },
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

// PreviewSmartTasksApp: Xem trước giao diện chính
@Preview(showBackground = true)
@Composable
fun PreviewSmartTasksApp() {
    SmartTasksTheme {
        val navController = rememberNavController()
        SmartTasksApp(navController)
    }
}