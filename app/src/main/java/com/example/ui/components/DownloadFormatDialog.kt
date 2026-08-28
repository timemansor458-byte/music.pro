package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Mp3AudioQuality
import com.example.model.YouTubeMusicVideo
import com.example.ui.theme.VibrantAccentAmber
import com.example.ui.theme.VibrantAccentCyan
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun DownloadFormatDialog(
  video: YouTubeMusicVideo,
  onDismiss: () -> Unit,
  onConfirmDownload: (Mp3AudioQuality) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedQuality by remember { mutableStateOf(Mp3AudioQuality.HQ_320) }

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("download_format_dialog"),
    shape = RoundedCornerShape(24.dp),
    containerColor = VibrantDarkSurface,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VibrantPurpleContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = VibrantPurple,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "تنزيل بصيغة MP3",
            color = VibrantTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
          Text(
            text = "الصيغ المدعومة: MP3 صوتي فقط",
            color = VibrantAccentCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Video Preview Title Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = VibrantDarkActiveSurface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(VibrantPurpleContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "🎵", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = video.title,
                color = VibrantTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1
              )
              Text(
                text = "${video.channelTitle} • ${video.duration}",
                color = VibrantTextMuted,
                fontSize = 11.sp
              )
            }
          }
        }

        Text(
          text = "اختر جودة الصوت (MP3 Bitrate):",
          color = VibrantTextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )

        // MP3 Quality Options
        Mp3AudioQuality.values().forEach { quality ->
          val isSelected = selectedQuality == quality
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) VibrantPurpleContainer.copy(alpha = 0.5f) else VibrantDarkActiveSurface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = if (isSelected) VibrantPurple else androidx.compose.ui.graphics.Color.Transparent,
                shape = RoundedCornerShape(14.dp)
              )
              .clickable { selectedQuality = quality }
              .testTag("quality_option_${quality.name}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) VibrantPurple else VibrantDarkBackground),
                  contentAlignment = Alignment.Center
                ) {
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = VibrantDarkBackground, modifier = Modifier.size(16.dp))
                  } else {
                    Icon(Icons.Default.Audiotrack, contentDescription = null, tint = VibrantTextMuted, modifier = Modifier.size(16.dp))
                  }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = quality.label,
                      color = VibrantTextPrimary,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(VibrantPurple.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(text = "MP3", color = VibrantPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                  Text(
                    text = "${quality.bitrate} • ${quality.estimatedSize}",
                    color = VibrantTextMuted,
                    fontSize = 11.sp
                  )
                }
              }
            }
          }
        }

        // MP3 Only Notice Banner
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(VibrantDarkBackground)
            .padding(8.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Security, contentDescription = null, tint = VibrantAccentCyan, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "يتم استخراج وتحويل الصوت نقيّاً بصيغة MP3 وحفظه في المكتبة",
              color = VibrantTextMuted,
              fontSize = 10.sp
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirmDownload(selectedQuality) },
        colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("confirm_mp3_download_btn")
      ) {
        Icon(Icons.Default.Download, contentDescription = null, tint = VibrantDarkBackground, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("بدء التحميل الآن", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("إلغاء", color = VibrantTextSecondary)
      }
    }
  )
}
