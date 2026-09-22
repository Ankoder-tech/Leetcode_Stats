package com.example.myapplication

import android.os.Bundle
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Orange
import kotlinx.coroutines.launch

val LightGray = Color(0xFFF7F9F8)
val DarkText = Color(0xFF202124)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Nav()
            }
        }
    }
}
@Composable
fun LoginScreen(
    onloginSuccess: (String) -> Unit
) { var username by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repository = remember {
        LeetCodeRepository()
    }
    Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        Image(painter = painterResource(id = R.drawable.leetcode),
            contentDescription = "LeetCode Logo",
            modifier = Modifier.size(130.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Welcome Back!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Login to continue your coding journey",
            fontSize = 15.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(value = username,
            onValueChange = {
                username = it
            },
            modifier = Modifier.fillMaxWidth(),

            label = { Text("LeetCode Username") },

            leadingIcon = {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = "Username",
                    tint = Orange
                )
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            enabled = !isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = LightGray,
                unfocusedContainerColor = LightGray
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
                val enteredUsername = username.trim()
                if (enteredUsername.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please enter your LeetCode username",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }
                coroutineScope.launch {

                    isLoading = true

                    try {
                        repository.getProfile(enteredUsername)
                        Toast.makeText(
                            context,
                            "Login Successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onloginSuccess(enteredUsername)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            e.message ?: "Unable to fetch LeetCode profile",
                            Toast.LENGTH_LONG
                        ).show()

                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange
            )

        ) {
            if (isLoading) { CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )

            } else {
                Text(
                    text = "Login",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))
        Row(verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "New here? ",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Text(text = "Sign Up", fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Orange
            )
        }
        Spacer(modifier = Modifier.height(65.dp))
        Text(text = "Solve Today,", fontSize = 18.sp,
            color = DarkText
        )

        Text(text = "Build a Better Tomorrow",
            fontSize = 18.sp,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(25.dp))
        Box(modifier = Modifier
                .width(60.dp)
                .height(3.dp)
                .background(Orange)
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}
@Composable
fun Nav() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {

            LoginScreen(
                onloginSuccess = { username ->
                    navController.navigate(
                        "home/${Uri.encode(username)}"
                    ) {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("home/{username}") { backStackEntry ->
            val username = backStackEntry
                .arguments
                ?.getString("username")
                ?: ""
            HomeScreen(
                username = username,
                backtologin = {
                    navController.navigate("login") {
                        popUpTo("home/{username}") {
                            inclusive = true
                        }
                    }
                },
                gotosubmissions = {
                    navController.navigate(
                        "submissions/${Uri.encode(username)}"
                    )
                }
            )
        }
        composable("submissions/{username}") { backStackEntry ->
            val username = backStackEntry
                .arguments
                ?.getString("username")
                ?: ""
            SubmissionScreen(username = username,
                backtohome = {
                    navController.popBackStack()
                }
            )
        }
    }
}