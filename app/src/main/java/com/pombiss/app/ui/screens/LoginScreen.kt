package com.pombiss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pombiss.app.data.LicenseManager
import com.pombiss.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    val isFormValid = username.isNotEmpty() && password.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = "https://raw.githubusercontent.com/Shr776/PomBiss/main/picon/PomBiss_logo.png",
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(80.dp).fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(text = "Welcome Back", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(24.dp))

        // یوزرنیم
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username", color = TextGray) },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // پسورد
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = TextGray) },
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
            modifier = Modifier.fillMaxWidth()
        )
        
        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMsg, color = NeonRed, fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // دکمه ورود
        Button(
            onClick = {
                isLoading = true
                errorMsg = ""
                scope.launch {
                    val success = LicenseManager.loginUser(context, username, password)
                    if (success) {
                        onLoginSuccess()
                    } else {
                        errorMsg = "Invalid username or password, or account is pending."
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && isFormValid
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // دکمه رفتن به ثبت‌نام
        TextButton(onClick = onGoToRegister) {
            Text("Don't have an account? Register here", color = TextGray, fontSize = 14.sp)
        }
    }
}
