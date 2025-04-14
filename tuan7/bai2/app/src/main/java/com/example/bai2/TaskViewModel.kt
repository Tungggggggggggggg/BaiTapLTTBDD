package com.example.bai2

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.bai2.TaskEntity
import com.example.bai2.TaskDao

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val backgroundColor: Color
)

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            taskDao.getAllTasks().collect { taskEntities ->
                _tasks.value = taskEntities.map { entity ->
                    Task(
                        id = entity.id,
                        title = entity.title,
                        description = entity.description,
                        backgroundColor = Color(entity.backgroundColor)
                    )
                }
            }
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            val newTask = TaskEntity(
                title = title,
                description = description,
                backgroundColor = Color(0xFFE6F0FA).value.toLong()
            )
            taskDao.insertTask(newTask)
        }
    }
}