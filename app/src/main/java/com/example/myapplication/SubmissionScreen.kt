package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.google.gson.JsonElement
val backgroundColor = Color(0xFF1A1A1A)
val submissionCardColor = Color(0xFF262626)
data class Submission(
    val date: String,
    val problemName: String,
    val result: String,
    val language: String
)
@Composable
fun SubmissionCard(submission: Submission,
    modifier: Modifier = Modifier
) {

    Card(modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),

        colors = CardDefaults.cardColors(
            containerColor = submissionCardColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = submission.date,
                color = Color.LightGray,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            Column(modifier = Modifier.weight(2.5f)
            ) {
                Text(text = submission.problemName,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = submission.language,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(text = submission.result,
                color = if (submission.result.equals(
                        "Accepted",
                        ignoreCase = true
                    )
                ) {
                    Color(0xFF00B8A3)
                } else {
                    Color.LightGray
                },
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionScreen(
    username: String,
    backtohome: () -> Unit
) {
    val repository = remember {
        LeetCodeRepository()
    }
    var submissions by remember {
        mutableStateOf<List<Submission>>(emptyList())
    }
    var isLoading by remember {
        mutableStateOf(true)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }
    LaunchedEffect(username) {
        isLoading = true
        errorMessage = ""
        try {val response = repository.getSubmissions(
                username = username,
                limit = 20
            )
            submissions = parseSubmissions(response)
        } catch (e: Exception) {
            errorMessage = e.message
                ?: "Unable to load submissions"

        } finally {

            isLoading = false
        }
    }
    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            backtohome()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Column { Text(text = "Submissions",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                        )

                        Text(text = username,
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = Color.White
                )
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
        ) {
            when {isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        color = Color(0xFFFFA116)
                    )
                }
                errorMessage.isNotEmpty() -> {
                    Text(text = errorMessage,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp)
                    )
                }
                submissions.isEmpty() -> {

                    Text(text = "No submissions found",
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
                else -> { LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(submissions) { submission ->
                            SubmissionCard(
                                submission = submission
                            )
                        }
                    }
                }
            }
        }
    }
}
fun parseSubmissions(
    response: JsonElement
): List<Submission> {
    if (!response.isJsonArray) {
        throw Exception("Unexpected submissions API response")
    }
    val jsonArray = response.asJsonArray
    return jsonArray.mapNotNull { element ->

        if (!element.isJsonObject) {
            return@mapNotNull null
        }
        val obj = element.asJsonObject
        val problemName = obj.get("title")
            ?.takeIf { !it.isJsonNull }
            ?.asString
            ?: obj.get("titleSlug")
                ?.takeIf { !it.isJsonNull }
                ?.asString
            ?: "Unknown Problem"
        val result = obj.get("statusDisplay")
            ?.takeIf { !it.isJsonNull }
            ?.asString
            ?: "Unknown"
        val language = obj.get("lang")
            ?.takeIf { !it.isJsonNull }
            ?.asString
            ?: "Unknown"
        val timestamp = obj.get("timestamp")
            ?.takeIf { !it.isJsonNull }
            ?.asString
            ?.toLongOrNull()
        val date = timestamp?.let {

            try {
                SimpleDateFormat(
                    "MMM dd",
                    Locale.getDefault()
                ).format(
                    Date(it * 1000L)
                )

            } catch (e: Exception) {
                "Unknown"
            }
        } ?: "Unknown"
        Submission(
            date = date,
            problemName = problemName,
            result = result,
            language = language
        )
    }
}