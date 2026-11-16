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
    
    // برند و مدل رسیور
    var selectedBrand by remember { mutableStateOf("") }
    var customBrand by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("") }
    var customModel by remember { mutableStateOf("") }
    
    var selectedImage by remember { mutableStateOf("") }
    var customImage by remember { mutableStateOf("") }

    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var countryExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    var brandExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }
    var imageExpanded by remember { mutableStateOf(false) }

    val countries = listOf("Iran", "Turkey", "Iraq", "Afghanistan", "Azerbaijan", "Other")
    val genders = listOf("Male", "Female")
    
    val images = listOf(
        "OpenATV", "OpenPLi", "OpenViX", "BlackHole", "VTi", 
        "Egami", "Pure2", "OpenBH", "OpenSpa", "Other (Type manually...)"
    )

    // لیست کامل‌تر برندها و مدل‌ها
    val brandModels = mapOf(
        "Vu+" to listOf("Zero", "Solo2", "Duo2", "Duo4K", "Duo4K SE", "Uno4K SE", "Zero 4K", "Uno", "Solo 4K", "Ultimo 4K", "Other"),
        "Dreambox" to listOf("DM500", "DM800", "DM8000", "DM900", "DM920", "One", "Two", "Other"),
        "Gigablue" to listOf("HD X1", "HD X2", "UHD UE 4K", "UHD Quad 4K", "UHD Trio 4K", "Other"),
        "Octagon" to listOf("SF8008", "SF8008 Twin", "SF4008", "SX88", "SX988", "Other"),
        "Zgemma" to listOf("H9", "H9S", "H9T", "H7", "H7S", "H11", "Other"),
        "Mutant" to listOf("HD51", "HD60", "HD66", "Other"),
        "Titan" to listOf("Titan 4K", "Other"),
        "Other (Type manually...)" to listOf("Other")
    )

    // بررسی اینکه آیا همه فیلدها پر شده‌اند یا نه
    val isFormValid = country.isNotEmpty() && 
                      gender.isNotEmpty() && 
                      age.isNotEmpty() &&
                      (selectedBrand.isNotEmpty() || customBrand.isNotEmpty()) && 
                      (selectedModel.isNotEmpty() || customModel.isNotEmpty()) && 
                      (selectedImage.isNotEmpty() || customImage.isNotEmpty())

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
                label = { Text("Country *", color = TextGray) },
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

        // ۲. مدل گوشی
        OutlinedTextField(
            value = phoneModel,
            onValueChange = { phoneModel = it },
            label = { Text("Phone Model", color = TextGray) },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // ۳. برند رسیور
        ExposedDropdownMenuBox(
            expanded = brandExpanded,
            onExpandedChange = { brandExpanded = !brandExpanded }
        ) {
            OutlinedTextField(
                value = selectedBrand,
                onValueChange = {},
                readOnly = true,
                label = { Text("Receiver Brand *", color = TextGray) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = brandExpanded, onDismissRequest = { brandExpanded = false }) {
                brandModels.keys.forEach { brand ->
                    DropdownMenuItem(text = { Text(brand) }, onClick = {
                        selectedBrand = brand
                        selectedModel = "" 
                        customModel = ""
                        if (brand != "Other (Type manually...)") customBrand = ""
                        brandExpanded = false
                    })
                }
            }
        }
        
        // کادر تایپ دستی برند (اگه گزینه Other انتخاب شد)
        if (selectedBrand == "Other (Type manually...)") {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customBrand,
                onValueChange = { customBrand = it },
                label = { Text("Type your receiver brand *", color = TextGray) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // ۴. مدل رسیور
        if (selectedBrand.isNotEmpty() && selectedBrand != "Other (Type manually...)") {
            val availableModels = brandModels[selectedBrand] ?: listOf("Other")
            
            ExposedDropdownMenuBox(
                expanded = modelExpanded,
                onExpandedChange = { modelExpanded = !modelExpanded }
            ) {
                OutlinedTextField(
                    value = selectedModel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Receiver Model *", color = TextGray) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = modelExpanded, onDismissRequest = { modelExpanded = false }) {
                    availableModels.forEach { model ->
                        DropdownMenuItem(text = { Text(model) }, onClick = {
                            selectedModel = model
                            if (model != "Other") customModel = "" 
                            modelExpanded = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // کادر تایپ دستی مدل رسیور
        if (selectedModel == "Other" || selectedBrand == "Other (Type manually...)") {
            OutlinedTextField(
                value = customModel,
                onValueChange = { customModel = it },
                label = { Text("Type your receiver model *", color = TextGray) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // ۵. ایمیج انیگما
        ExposedDropdownMenuBox(
            expanded = imageExpanded,
            onExpandedChange = { imageExpanded = !imageExpanded }
        ) {
            OutlinedTextField(
                value = selectedImage,
                onValueChange = {},
                readOnly = true,
                label = { Text("Enigma2 Image *", color = TextGray) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = imageExpanded) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = imageExpanded, onDismissRequest = { imageExpanded = false }) {
                images.forEach { image ->
                    DropdownMenuItem(text = { Text(image) }, onClick = {
                        selectedImage = image
                        if (image != "Other (Type manually...)") customImage = ""
                        imageExpanded = false
                    })
                }
            }
        }
        
        // کادر تایپ دستی ایمیج
        if (selectedImage == "Other (Type manually...)") {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customImage,
                onValueChange = { customImage = it },
                label = { Text("Type your image name *", color = TextGray) },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // ۶. سن
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age *", color = TextGray) },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // ۷. جنسیت
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded }
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender *", color = TextGray) },
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

        // دکمه ثبت‌نام (تا وقتی همه فیلدها پر نشن غیرفعاله)
        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    try {
                        val finalBrand = if (selectedBrand == "Other (Type manually...)") customBrand else selectedBrand
                        val finalModel = if (selectedModel == "Other" || selectedBrand == "Other (Type manually...)") customModel else selectedModel
                        val finalImage = if (selectedImage == "Other (Type manually...)") customImage else selectedImage
                        
                        LicenseManager.registerDeviceWithDetails(
                            context, country, phoneModel, "$finalBrand $finalModel", finalImage, age, gender
                        )
                        onRegistrationSuccess()
                    } catch (e: Exception) {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) MaterialTheme.colorScheme.primary else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && isFormValid
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
