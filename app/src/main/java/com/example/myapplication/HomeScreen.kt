package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Orange
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.google.gson.JsonArray
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign
@Composable
fun HomeScreen(username: String, backtologin: () -> Unit, gotosubmissions: () -> Unit) {
    val backgroundColor = Color(0xFF1A1A1A)
    val cardColor = Color(0xFF262626)
    val repository = remember { LeetCodeRepository() }
    val scope = rememberCoroutineScope()
    var profile by remember { mutableStateOf<JsonObject?>(null) }
    var solved by remember { mutableStateOf<JsonObject?>(null) }
    var badgesResponse by remember { mutableStateOf<JsonObject?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var error by remember { mutableStateOf<String?>(null) }
    suspend fun loadData() { isLoading = true
        error = null
        try { val profileResponse = repository.getProfile(username)
            val solvedResponse = repository.getSolved(username)
            val badgesApiResponse = repository.getBadges(username)
            profile = profileResponse
            solved = solvedResponse
            badgesResponse = badgesApiResponse
        } catch (e: Exception) { error = e.message ?: "Unable to fetch LeetCode data"
        } finally { isLoading = false }
    }
    LaunchedEffect(username) { loadData() }

    Column(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
             .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        IconButton(onClick = backtologin) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "My LeetCode Stats",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Orange)
            }

        } else if (error != null) {
            Text(text = error ?: "Something went wrong",
                color = Color.Red,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                    scope.launch {
                        loadData()
                    }
                }
            ) {
                Text("Retry")
            }

        } else if (profile != null && solved != null) {
            val profileData = profile!!
            val solvedData = solved!!
            val badgesData = badgesResponse
                ?.getAsJsonArray("badges")
                ?: JsonArray()
            val apiUsername = profileData.getStringValue(
                "username",
                username
            )
            val realName = profileData.getStringValue(
                "name",
                "LeetCode User"
            )
            val avatarUrl = profileData.getStringValue("avatar")
            val ranking = profileData.getIntValue("ranking")
            val totalSolved = solvedData.getIntValue("solvedProblem")
            val totalQuestions = 4059
            val easySolved = solvedData.getIntValue("easySolved")
            val totalEasy = 966
            val mediumSolved = solvedData.getIntValue("mediumSolved")
            val totalMedium = 2117
            val hardSolved = solvedData.getIntValue("hardSolved")
            val totalHard = 976
            val progress = if (totalQuestions > 0) {
                (totalSolved.toFloat() / totalQuestions)
                    .coerceIn(0f, 1f)
            } else { 0f }
            Card(modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(model = avatarUrl,
                        contentDescription = "LeetCode Profile Picture",
                        modifier = Modifier
                            .size(85.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = apiUsername,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = realName,
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Rank: ${
                                if (ranking > 0) ranking.toString()
                                else "N/A"
                            }",
                            color = Color.LightGray,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "LeetCode Profile",
                            color = Orange,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)
                ) {

                    Text(text = "Problems Solved",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "$totalSolved / $totalQuestions",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Orange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        color = Orange,
                        trackColor = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${(progress * 100).toInt()}% Completed",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Problem Difficulty",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DifficultyCard(title = "Easy",
                    solved = easySolved.toString(),
                    total = totalEasy.toString(),
                    color = Color(0xFF00B8A3),
                    modifier = Modifier.weight(1f)
                )
                DifficultyCard(title = "Medium", solved = mediumSolved.toString(),
                    total = totalMedium.toString(),
                    color = Color(0xFFFFC01E),
                    modifier = Modifier.weight(1f)
                )

                DifficultyCard(title = "Hard",
                    solved = hardSolved.toString(),
                    total = totalHard.toString(),
                    color = Color(0xFFFF375F),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "My Badges",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (badgesData.size() == 0) {
                        Text(text = "No badges found",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )

                    } else { LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                        items(
                               badgesData
                                    .asList()
                                    .filter { it.isJsonObject }
                            ) { badgeElement ->
                                val badge = badgeElement.asJsonObject
                                val badgeName = badge.getStringValue(
                                        "displayName",
                                        badge.getStringValue(
                                            "name",
                                            "LeetCode Badge"
                                        )
                                    )
                                val badgeIcon =
                                    badge.getStringValue("icon")

                                Column(
                                    modifier = Modifier.width(90.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(model = badgeIcon,
                                        contentDescription = badgeName,
                                        modifier = Modifier.size(65.dp),
                                        contentScale = ContentScale.Fit
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = badgeName,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        gotosubmissions()
                    },
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)
                ) {

                    Text(text = "Recent Submissions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "View your recent submissions →",
                        fontSize = 15.sp,
                        color = Orange
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
@Composable
fun DifficultyCard(
    title: String,
    solved: String,
    total: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Card(modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF262626)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = title,
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = solved,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "/ $total",
                color = Color.LightGray,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            val solvedValue = solved.toFloatOrNull() ?: 0f
            val totalValue = total.toFloatOrNull() ?: 0f
            val difficultyProgress = if (totalValue > 0f) {
                (solvedValue / totalValue).coerceIn(0f, 1f)
            } else {0f}

            LinearProgressIndicator(
                progress = { difficultyProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = color,
                trackColor = Color.DarkGray
            )
        }
    }
}

fun JsonObject.getStringValue(key: String, defaultValue: String = ""): String {
    return try {
        if (has(key) && !get(key).isJsonNull) {
            get(key).asString
        } else {
            defaultValue
        }
    } catch (e: Exception) {
        defaultValue
    }
}
fun JsonObject.getIntValue(key: String,
    defaultValue: Int = 0
): Int {
    return try {
        if (has(key) && !get(key).isJsonNull) {
            get(key).asInt
        } else {
            defaultValue
        }
    } catch (e: Exception) {
        defaultValue
    }
}