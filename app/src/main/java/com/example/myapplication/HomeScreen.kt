package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.example.myapplication.ui.theme.Orange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun HomeScreen(backtohome: () -> Unit) {
    val backgroundColor = Color(0xFF1A1A1A)
    val cardColor = Color(0xFF262626)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        IconButton(onClick = { backtohome() }
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "My LeetCode Stats", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        Card(elevation= CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(12.dp)
        ) {

            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

                Image(painter = painterResource(id = R.drawable.profile1),
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(70.dp)
                        .weight(2f)
                )

                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(8f)) {
                    Text(text = "Ankit Shukla", color = Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                    Text(text = "Ankoder", color = Color.LightGray,
                        fontSize = 13.sp
                    )
                    Text(text = "Rank: 1,531,305",
                        color = Color.LightGray,
                        fontSize = 18.sp
                    )


                }
            }

            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "0 Following", color = Color.LightGray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier
                    .width(3.dp)
                    .height(15.dp)
                    .background(Orange)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "0 Follower", color = Color.LightGray,
                    fontSize = 13.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(elevation= CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Problems Solved", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "111 / 4059", fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(progress ={ 111f / 4059f}, modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = Orange,
                    trackColor = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "3 Attempting",
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
                solved = "54",
                total = "966",
                color = Color(0xFF00B8A3),
                modifier = Modifier.weight(1f)
            )
            DifficultyCard(title = "Medium",
                solved = "50",
                total = "2117",
                color = Color(0xFFFFC01E),
                modifier = Modifier.weight(1f)
            )
            DifficultyCard(
                title = "Hard",
                solved = "7",
                total = "976",
                color = Color(0xFFFF375F),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Activity Stats",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "266 Submissions in the past year",
                    color = Color.LightGray,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Total Active Days: 70", color = Orange,
                    fontSize = 16.sp
                )
                Text(
                    text = "Max Streak: 15 Day",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Recent Submissions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Min Cost to Connect All Points",
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Accepted  •  9 months ago",
                        fontSize = 14.sp,
                        color = Orange
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))





    }

}

@Composable
fun DifficultyCard(title: String, solved: String, total: String, color: Color, modifier: Modifier = Modifier) {

    Card(modifier = modifier.clickable{},
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF262626)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(text = title,
                    color = color,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false
                )

                DifficultyCircle(color)
            }

            Text(text = "$solved / $total",
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                softWrap = false
            )
        }


    }
}
@Composable
fun DifficultyCircle(color: Color) {
    Box(modifier = Modifier.size(15.dp)
            .background(
                color = color,
                shape = CircleShape
            )
    )
}


