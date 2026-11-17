package com.pombiss.app.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pombiss.app.data.Feed
import com.pombiss.app.ui.theme.*

@Composable
fun FeedCard(feed: Feed, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.8f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            
            // ۱. ردیف بالا: عنوان فید (چپ) و ماهواره (راست)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // عنوان فید (با هشتگ و رنگ صورتی/بنفش)
                Text(
                    text = if (feed.title.isNotBlank()) feed.title else "Unknown Event",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonPink,
                    modifier = Modifier.weight(1f)
                )
                
                // اسم ماهواره (بزرگ و برجسته در سمت راست)
                Text(
                    text = feed.sat,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // ۲. مشخصات فرکانس (فرکانس بولد و بزرگ، بقیه رنگی)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // فرکانس (زرد و بولد)
                Text(
                    text = feed.freq, 
                    fontSize = 22.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    color = NeonYellow
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "•", color = TextGray)
                Spacer(modifier = Modifier.width(8.dp))
                // سیمبولریت (سبز)
                Text(
                    text = feed.sr, 
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = NeonGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "•", color = TextGray)
                Spacer(modifier = Modifier.width(8.dp))
                // پلاریزاسیون (آبی روشن)
                Text(
                    text = feed.pol, 
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = NeonCyan
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // ۳. اسم فید
            Text(
                text = if (feed.feedId.isNotBlank()) feed.feedId else "No Feed ID",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // ۴. ردیف پایین: کد فید، دکمه کپی و تاریخ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // کد فید (نارنجی)
                    Text(
                        text = feed.cw,
                        fontSize = 14.sp,
                        color = NeonOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Feed Info", feed.toShareString())
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "اطلاعات فید کپی شد!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(text = feed.datetime, fontSize = 12.sp, color = TextGray)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            // فرمت فید (خاکستری)
            Text(text = feed.type, fontSize = 13.sp, color = TextGray)
        }
    }
}
