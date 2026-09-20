package com.example.myapplication

import android.R.attr.top
import androidx.compose.ui.graphics.Color
import android.graphics.Color.WHITE
import android.os.Bundle
import android.service.controls.Control
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.GreenJC
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                val navController = rememberNavController()

                NevGraph(
                    navController = navController
                )

            }
        }

    }
}

@Composable
fun LoginScreen(onloginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext

    Column(modifier = Modifier.fillMaxSize()
        .padding(horizontal = 26.dp, vertical = 140.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            painter = painterResource(id = R.drawable.leetcode),
            contentDescription = "Login Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 20.dp),
            contentScale = ContentScale.Fit
        )
        OutlinedTextField(value=username,onValueChange = {username=it },
            label = { Text("Username") },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedLeadingIconColor = GreenJC,
                unfocusedLeadingIconColor = GreenJC,
                focusedLabelColor = GreenJC,
                unfocusedLabelColor = GreenJC,
                focusedContainerColor= White,
                unfocusedContainerColor = White,
                unfocusedIndicatorColor = GreenJC,
                unfocusedPlaceholderColor = GreenJC
            ), leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription ="Username")
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(value=password,onValueChange = {password=it },
            label = { Text("Password") },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedLeadingIconColor = GreenJC,
                unfocusedLeadingIconColor = GreenJC,
                focusedLabelColor = GreenJC,
                unfocusedLabelColor = GreenJC,
                focusedContainerColor= White,
                unfocusedContainerColor = White,
                unfocusedIndicatorColor = GreenJC,
                unfocusedPlaceholderColor = GreenJC
            ), leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription ="Password")
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            visualTransformation = PasswordVisualTransformation(),
        )
        Button(onClick = {
            if (authenticate(username, password)) {
                onloginSuccess()
                Toast.makeText(context,"Login Successful",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Login Failed",Toast.LENGTH_SHORT).show()
            }
        }, colors = ButtonDefaults.buttonColors(GreenJC),
            contentPadding = PaddingValues(start=60.dp,end=60.dp,top=8.dp,bottom=8.dp),
            modifier=Modifier.padding(top=18.dp)) {
            Text(text = "Login", fontSize = 22.sp)

        }
    }
}
private fun authenticate(username: String, password: String) : Boolean{
    val validUsername="admin"
    val validPassword="password"
    return username==validUsername && password==validPassword
}

@Composable
fun NevGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = Modifier.fillMaxSize()
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




