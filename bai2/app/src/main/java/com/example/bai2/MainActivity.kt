package com.example.bai2

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var employeeNameEditText: EditText
    private lateinit var changeEmployeeButton: Button
    private lateinit var bookListCheckBoxes: List<CheckBox>
    private lateinit var addButton: Button
    private lateinit var employeeLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        employeeNameEditText = findViewById(R.id.employee_name)
        changeEmployeeButton = findViewById(R.id.change_employee)
        bookListCheckBoxes = listOf(
            findViewById(R.id.book1_checkbox),
            findViewById(R.id.book2_checkbox)
        )
        addButton = findViewById(R.id.add_button)
        employeeLabel = findViewById(R.id.employee_label)

        changeEmployeeButton.setOnClickListener {
            val newEmployeeName = employeeNameEditText.text.toString()
            if (newEmployeeName.isNotBlank()) {
                employeeLabel.text = "Nhân viên: $newEmployeeName"
                Toast.makeText(this, "Tên nhân viên đã được cập nhật", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Vui lòng nhập tên nhân viên", Toast.LENGTH_SHORT).show()
            }
        }

        addButton.setOnClickListener {
            val selectedBooks = mutableListOf<String>()
            for (checkBox in bookListCheckBoxes) {
                if (checkBox.isChecked) {
                    selectedBooks.add(checkBox.text.toString())
                }
            }

            if (selectedBooks.isNotEmpty()) {
                val booksList = selectedBooks.joinToString(", ")
                Toast.makeText(this, "Sách đã được thêm: $booksList", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Vui lòng chọn ít nhất một cuốn sách", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
