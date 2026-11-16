package com.pombiss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pombiss.app.data.LicenseManager
import com.pombiss.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var country by remember { mutableStateOf("") }
    var phoneModel by remember { mutableStateOf(LicenseManager.getDeviceModel()) }
    var receiverModel by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var countryExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var receiverExpanded by remember { mutableStateOf(false) }

    val countries = listOf("Iran", "Turkey", "Iraq", "Afghanistan", "Azerbaijan", "Other")
    val genders = listOf("Male", "Female")
    val receivers = listOf(
        "Vu+ Zero", "Vu+ Solo2", "Vu+ Duo4K", "Vu+ Uno4K",
        "Dreambox 800", "Dreambox 900", "Octagon SF8008",
        "Zgemma H9", "Zgemma H7", "Mutant HD51", "Other"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        AsyncImage(
            model = "https://raw.githubusercontent.com/Shr776/PomBiss/main/picon/PomBiss_logo.png",
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(70.dp).fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Registration Form",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // ۱. کشور
        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = !countryExpanded }
        ) {
            OutlinedTextField(
                value = country,
                onValueChange = {},
                readOnly = true,
                label = { Text("Country", color = TextGray) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = countryExpanded, onDismissRequest = { countryExpanded = false }) {
                countries.forEach { selection ->
                    DropdownMenuItem(text = { Text(selection) }, onClick = {
                        country = selection
                        countryExpanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // ۲. مدل گوشی (خودکار پر میشه)
        OutlinedTextField(
            value = phoneModel,
            onValueChange = { phoneModel = it },
            label = { Text("Phone Model", color = TextGray) },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // ۳. مدل رسیور (لیست کشویی)
        ExposedDropdownMenuBox(
            expanded = receiverExpanded,
            onExpandedChange = { receiverExpanded = !receiverExpanded }
        ) {
            OutlinedTextField(
                value = receiverModel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Receiver Model", color = TextGray) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = receiverExpanded) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = receiverExpanded, onDismissRequest = { receiverExpanded = false }) {
                receivers.forEach { selection ->
                    DropdownMenuItem(text = { Text(selection) }, onClick = {
                        receiverModel = selection
                        receiverExpanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // ۴. سن (تایپی)
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age", color = TextGray) },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // ۵. جنسیت (لیست کشویی)
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded }
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender", color = TextGray) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = genderExpanded, onDismissRequest = { genderExpanded = false }) {
                genders.forEach { selection ->
                    DropdownMenuItem(text = { Text(selection) }, onClick = {
                        gender = selection
                        genderExpanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // دکمه ثبت‌نام
        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    try {
                        LicenseManager.registerDeviceWithDetails(
                            context, country, phoneModel, receiverModel, age, gender
                        )
                        onRegistrationSuccess()
                    } catch (e: Exception) {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && country.isNotEmpty() && gender.isNotEmpty() && receiverModel.isNotEmpty()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Submit & Wait for Approval", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
