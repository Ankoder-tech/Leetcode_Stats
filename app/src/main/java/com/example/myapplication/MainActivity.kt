package com.example.myapplication

import android.os.Bundle
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Orange

val Green = Color(0xFF2DA44E)
val LightGray = Color(0xFFF7F9F8)
val DarkText = Color(0xFF202124)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {

                        LoginScreen(
                            onloginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable("home") {
                        HomeScreen()
                    }
                }
            }
        }
    }
}
@Composable
fun LoginScreen(onloginSuccess: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(modifier = Modifier.fillMaxSize()
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
        Text(text = "Login to continue your coding journey",
            fontSize = 15.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Username") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person,
                    contentDescription = "Username",
                    tint = Orange
                )
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = LightGray,
                unfocusedContainerColor = LightGray
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = Orange
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = LightGray,
                unfocusedContainerColor = LightGray
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                if (authenticate(username, password)) {
                    Toast.makeText(
                        context,
                        "Login Successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                    onloginSuccess()
                } else{
                    Toast.makeText(
                        context,
                        "Invalid Username or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange
            )
        ) {
            Text(text = "Login",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "New here? ",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Text(text = "Sign Up",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Orange
            )
        }
        Spacer(modifier = Modifier.height(65.dp))

        Text(text = "Solve Today,",
            fontSize = 18.sp,
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


private fun authenticate(username: String, password: String): Boolean {
    val validUsername = "admin"
    val validPassword = "password"
    return username == validUsername && password == validPassword
}