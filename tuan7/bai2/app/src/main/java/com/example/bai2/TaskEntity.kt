package com.example.bai2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val backgroundColor: Long // Lưu màu dưới dạng Long vì Room không hỗ trợ Color trực tiếp
)