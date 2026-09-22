package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
private val backgroundColor = Color(0xFF1A1A1A)
private val submissionCardColor = Color(0xFF262626)
private val acceptedColor = Color(0xFF00B8A3)
private val accentColor = Color(0xFFFFA116)
data class Submission(
    val id: String,
    val date: String,
    val problemName: String,
    val result: String,
    val language: String
)
@Composable
fun SubmissionCard(
    submission: Submission,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = submissionCardColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = submission.date,
                color = Color.LightGray,
                fontSize = 12.sp,

                modifier = Modifier.weight(1.2f)
            )
            Column(
                modifier = Modifier
                    .weight(2.5f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = submission.problemName,
                    color = Color.White,
                    fontSize = 14.sp,
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
                ) { acceptedColor
                } else {
                    Color.LightGray
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionScreen(username: String, backtohome: () -> Unit) {
    val repository = remember {
        LeetCodeRepository()
    }
    var submissions by remember(username) {
        mutableStateOf<List<Submission>>(emptyList())
    }
    var isLoading by remember(username) {
        mutableStateOf(true)
    }
    var errorMessage by remember(username) {
        mutableStateOf<String?>(null)
    }
    var retryCount by remember(username) {
        mutableStateOf(0)
    }
    LaunchedEffect(username, retryCount) {
        isLoading = true
        errorMessage = null
        try {val response = withTimeout(30_000L) {
                repository.getSubmissions(
                    username = username,
                    limit = 20
                )
            }
            submissions = parseSubmissions(response)
        } catch (e: Exception) {
            errorMessage = e.message
                ?: "Unable to load submissions"
        } finally {isLoading = false }
    }
    Scaffold(containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = backtohome) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Column {
                        Text(text = "Recent Submissions",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = username,
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }

    ) { innerPadding ->
        Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)

        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    ),
                    color = accentColor
                )
            }
            else if (errorMessage != null) {
                Column(modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(text = errorMessage ?: "Something went wrong",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { retryCount++ },
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text(text = "Retry",
                            color = Color.Black
                        )
                    }
                }
            }
            else if (submissions.isEmpty()) {
                Text(text = "No submissions found",
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
            else {

                LazyColumn(modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    items(items = submissions,
                        key = { it.id }
                    ) { submission ->
                        SubmissionCard(
                            submission = submission
                        )
                    }
                }
            }
        }
    }
}

fun parseSubmissions(
    response: JsonElement
): List<Submission> {
    if (!response.isJsonObject) {
        throw Exception(
            "Invalid API response format"
        )
    }
    val jsonObject = response.asJsonObject
    val jsonArray = jsonObject
        .get("submission")
        ?.takeIf { it.isJsonArray }
        ?.asJsonArray
        ?: throw Exception(
            "Submission array not found in API response"
        )
    return jsonArray.mapIndexedNotNull { index, element ->
        if (!element.isJsonObject) {
            return@mapIndexedNotNull null
        }
        val obj = element.asJsonObject
        val problemName = getJsonString(obj, "title")
            ?: getJsonString(obj, "titleSlug") ?: "Unknown Problem"
        val result = getJsonString(obj, "statusDisplay") ?: "Unknown"
        val language = getJsonString(obj, "lang") ?: "Unknown"
        val timestamp = getJsonString(obj, "timestamp")?.toLongOrNull()
        val date = timestamp?.let {
            SimpleDateFormat(
                "MMM dd, yyyy",
                Locale.getDefault()
            ).format(
                Date(it * 1000L)
            )

        } ?: "Unknown Date"
        val id = "$index-$timestamp-$problemName"
        Submission(id = id,
            date = date,
            problemName = problemName,
            result = result,
            language = language
        )
    }
}
private fun getJsonString(obj: JsonObject, key: String): String? {
    return obj.get(key)
        ?.takeIf { !it.isJsonNull && it.isJsonPrimitive }
        ?.asString
}