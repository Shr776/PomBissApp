package com.pombiss.app.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // بخش سمت چپ (اطلاعات)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // عنوان فید
                Text(
                    text = feed.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(6.dp))

                // فرکانس، SR و پلاریزاسیون
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = feed.freq, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NeonYellow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "•", color = TextGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = feed.sr, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "•", color = TextGray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = feed.pol, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // اسم فید
                Text(
                    text = if (feed.feedId.isNotBlank()) feed.feedId else feed.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // ردیف کد فید و دکمه کپی
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = feed.cw, fontSize = 13.sp, color = NeonOrange, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Feed Info", feed.toShareString())
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "اطلاعات کپی شد!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // تاریخ و فرمت
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = feed.type, fontSize = 12.sp, color = TextGray)
                    Text(text = feed.datetime, fontSize = 11.sp, color = TextGray)
                }
            }

            // خط عمودی جداکننده
            VerticalDivider(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .height(70.dp), // ارتفاعش رو یه کوچولو کمتر کردم که با محتوا هم‌تراز باشه
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            // ماهواره (با فونت کوچیک‌تر و فاصله مناسب)
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .padding(vertical = 4.dp), // فاصله از بالا و پایین
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = feed.sat,
                    fontSize = 28.sp, // از 34 به 28 کاهش دادم
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp // فاصله خطوط رو تنظیم کردم
                )
            }
        }
    }
}
