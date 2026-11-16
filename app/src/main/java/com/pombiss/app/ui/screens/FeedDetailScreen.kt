package com.pombiss.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.data.Feed
import com.pombiss.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedDetailScreen(feed: Feed, onBack: () -> Unit) {
    val context = LocalContext.current

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
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // نام ماهواره
            Text(
                text = feed.sat,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // مشخصات فرکانس، SR و پلاریزاسیون
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(text = feed.freq, fontSize = 34.sp, fontWeight = FontWeight.Black, color = NeonYellow)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "•", fontSize = 34.sp, color = TextGray)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = feed.sr, fontSize = 30.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "•", fontSize = 34.sp, color = TextGray)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = feed.pol, fontSize = 30.sp, fontWeight = FontWeight.Black, color = NeonGreen)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // اسم فید
            Text(
                text = if (feed.feedId.isNotBlank()) feed.feedId else feed.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // فرمت
            Text(
                text = feed.type,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOrange
            )

            Spacer(modifier = Modifier.height(20.dp))

            // کد فید (CW) با طراحی برجسته و دکمه کپی
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, NeonPink.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CW: ${feed.cw}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPink
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("CW", feed.cw)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "کد کپی شد!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy CW", tint = TextWhite)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // تاریخ و ساعت
            Text(
                text = feed.datetime,
                fontSize = 16.sp,
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
