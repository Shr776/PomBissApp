package com.pombiss.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.data.Feed
import com.pombiss.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedDetailScreen(feed: Feed, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feed Details", color = TextWhite) },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // نام ماهواره (بزرگ‌ترین)
            Text(
                text = feed.sat,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // مشخصات فرکانس، SR و پلاریزاسیون
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(text = feed.freq, fontSize = 36.sp, fontWeight = FontWeight.Black, color = NeonYellow)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "•", fontSize = 36.sp, color = TextGray)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = feed.sr, fontSize = 32.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "•", fontSize = 36.sp, color = TextGray)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = feed.pol, fontSize = 32.sp, fontWeight = FontWeight.Black, color = NeonGreen)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // اسم فید
            Text(
                text = if (feed.feedId.isNotBlank()) feed.feedId else "No Feed ID",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // فرمت
            Text(
                text = feed.type,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOrange
            )

            Spacer(modifier = Modifier.height(16.dp))

            // تاریخ و ساعت
            Text(
                text = feed.datetime,
                fontSize = 18.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.weight(1f))

            // فوتر تبلیغاتی
            HorizontalDivider(color = NeonBlue.copy(alpha = 0.3f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = "Developed by @Shr776", color = NeonPink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "Channel: @VuSolo | Support: @PomSat", color = TextGray, fontSize = 12.sp)
            Text(text = "Feeds: @PomFeed | @FeedHunter", color = TextGray, fontSize = 12.sp)
            Text(text = "YouTube: @VUSOLO | GitHub: Shr776 / PomBiss", color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
