package com.pombiss.app.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.data.LicenseManager
import com.pombiss.app.ui.theme.*
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var username by remember { mutableStateOf(LicenseManager.getUsername(context)) }
    var newPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    // متغیر برای نگه داشتن عکس انتخاب شده
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // لانچر برای انتخاب عکس از گالری
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        if (uri != null) {
            // کپی کردن عکس به حافظه داخلی برنامه
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile_pic.jpg")
            val outputStream = file.outputStream()
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        }
    }

    // لود کردن عکس فعلی (اگه وجود داشته باشه)
    val currentProfilePic = remember {
        val file = File(context.filesDir, "profile_pic.jpg")
        if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
        } else null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // بخش عکس پروفایل
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(SurfaceDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    // اگه عکس جدید انتخاب شده
                    val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(selectedImageUri!!))?.asImageBitmap()
                    if (bitmap != null) {
                        Image(bitmap = bitmap, contentDescription = "Profile", modifier = Modifier.fillMaxSize().background(CircleShape))
                    }
                } else if (currentProfilePic != null) {
                    // اگه عکس قبلی وجود داره
                    Image(bitmap = currentProfilePic, contentDescription = "Profile", modifier = Modifier.fillMaxSize())
                } else {
                    // آیکون پیش‌فرض
                    Icon(Icons.Default.Person, contentDescription = "Default", tint = TextGray, modifier = Modifier.size(50.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // دکمه انتخاب عکس
            TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Change Profile Picture", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // فیلد نام کاربری
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = TextGray) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // فیلد پسورد جدید
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password (Optional)", color = TextGray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isLoading = true
                    LicenseManager.saveUsername(context, username)
                    isLoading = false
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading && username.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
