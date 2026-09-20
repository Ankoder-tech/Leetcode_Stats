package com.example.myapplication


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.NavigationBar
data class Submission(
    val date: String,
    val problemNumber: Int,
    val problemName: String,
    val difficulty: String,
    val result: String,
    val submissions: Int
)

val backgroundColor = Color(0xFF1A1A1A)
@Composable
fun SubmissionCard(
    submission: Submission,
    modifier: Modifier = Modifier
) {

                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF262626)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = submission.date,
                            color = Color.LightGray,
                            modifier = Modifier.weight(1f)
                        )

                        Column(
                            modifier = Modifier.weight(2.5f)
                        ) {
                            Text(
                                text = "${submission.problemNumber}. ${submission.problemName}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = submission.difficulty,
                                color = when (submission.difficulty) {
                                    "Easy" -> Color(0xFF00B8A3)
                                    "Medium" -> Color(0xFFFFC01E)
                                    "Hard" -> Color(0xFFFF375F)
                                    else -> Color.Gray
                                },
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = submission.result, color = Color.LightGray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = submission.submissions.toString(),
                            color = Color.LightGray,
                            modifier = Modifier.weight(0.3f)
                        )
                    }
                }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionScreen() {

    val submissions = listOf(

        Submission("Sep 15",
            225,
            "Implement Stack using Queues",
            "Easy",
            "Accepted",
            1
        ),

        Submission(
            "Aug 30",
            37,
            "Sudoku Solver",
            "Hard",
            "Accepted",
            1
        ),
        Submission("Aug 30",
            51,
            "N-Queens",
            "Hard",
            "Accepted",
            2
        ),
        Submission(
            "Aug 25",
            90,
            "Subsets II",
            "Medium",
            "Accepted",
            1
        ),
        Submission(
            "Aug 25",
            40,
            "Combination Sum II",
            "Medium",
            "Accepted",
            1
        ),
        Submission(
            "Aug 25",
            39,
            "Combination Sum",
            "Medium",
            "Accepted",
            1
        ),
        Submission(
            "Aug 25",
            40,
            "Pow(x,n",
            "Medium",
            "Accepted",
            7
        ),
        Submission(
            "Aug 13",
            202,
            "Happy Number",
            "Easy",
            "Accepted",
            1
        ),
        Submission(
            "Aug 13",
            367,
            "Valid Perfect Square",
            "Easy",
            "Accepted",
            3
        ),Submission(
            "Aug 12",
            386,
            "Lexicographical Numbers",
            "Medium",
            "Accepted",
            3

        ),
        Submission(
            "Aug 12",
            509,
            "Fibonacci Number",
            "Easy",
            "Accepted",
            3

        ),


    )

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        topBar = {
            TopAppBar(
                title = { Text("Submissions") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1A1A1A)
            ) {
            }
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            contentPadding = PaddingValues(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 16.dp
            ),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(submissions) { submission ->
                SubmissionCard(submission = submission)
            }
        }
    }
}
