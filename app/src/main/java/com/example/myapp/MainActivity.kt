package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudentReportScreen()
                }
            }
        }
    }
}

@Composable
fun StudentReportScreen() {

    var name by remember { mutableStateOf("") }
    val subjects = remember { mutableStateListOf("", "", "") }
    var result by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Student Performance Report",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        subjects.forEachIndexed { index, mark ->

            OutlinedTextField(
                value = mark,
                onValueChange = { subjects[index] = it },
                label = { Text("Subject ${index + 1} Marks") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {

                if (name.isBlank()) {
                    result = "Please enter student name"
                    return@Button
                }

                if (subjects.any { it.isBlank() }) {
                    result = "Please enter all subject marks"
                    return@Button
                }

                val marks = subjects.map { it.toIntOrNull() ?: 0 }

                if (marks.any { it < 0 || it > 100 }) {
                    result = "Marks must be between 0 and 100"
                    return@Button
                }

                val total = marks.sum()
                val average = total.toDouble() / marks.size

                val grade = when {
                    average >= 90 -> "A+"
                    average >= 80 -> "A"
                    average >= 70 -> "B"
                    average >= 60 -> "C"
                    average >= 50 -> "D"
                    else -> "F"
                }

                val passFail = if (average >= 50) "Pass" else "Fail"

                result =
                    "Name: $name\n" +
                            "Total: $total\n" +
                            "Average: ${String.format("%.2f", average)}\n" +
                            "Grade: $grade\n" +
                            "Result: $passFail"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        Button(
            onClick = {
                subjects.add("")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Subject")
        }

        Button(
            onClick = {
                name = ""
                subjects.clear()
                subjects.addAll(listOf("", "", ""))
                result = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
        }

        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = result,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentReportPreview() {
    MaterialTheme {
        StudentReportScreen()
    }
}
