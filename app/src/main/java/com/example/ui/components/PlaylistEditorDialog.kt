package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.model.Playlist
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantDarkSurfaceVariant
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary

@Composable
fun PlaylistEditorDialog(
  playlist: Playlist?,
  onDismiss: () -> Unit,
  onSave: (name: String, description: String, iconEmoji: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val isCreating = playlist == null
  var name by remember { mutableStateOf(playlist?.name ?: "") }
  var description by remember { mutableStateOf(playlist?.description ?: "") }
  var selectedEmoji by remember { mutableStateOf(playlist?.iconEmoji ?: "🎶") }

  val emojiOptions = listOf("🎶", "🎵", "🎧", "✨", "🔥", "🌙", "🎻", "❤️", "⚡", "🌟", "🎹", "🎙️", "🎸", "🌊")

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("playlist_editor_dialog"),
    shape = RoundedCornerShape(24.dp),
    containerColor = VibrantDarkSurface,
    title = {
      Text(
        text = if (isCreating) "إنشاء قائمة تشغيل جديدة" else "تعديل تفاصيل القائمة",
        color = VibrantTextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "اختر أيقونة القائمة:",
          color = VibrantTextMuted,
          fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(emojiOptions) { emoji ->
            val isSelected = emoji == selectedEmoji
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(if (isSelected) VibrantPurpleContainer else VibrantDarkSurfaceVariant)
                .clickable { selectedEmoji = emoji },
              contentAlignment = Alignment.Center
            ) {
              Text(text = emoji, fontSize = 20.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("اسم القائمة") },
          placeholder = { Text("مثلاً: أغانٍ للمساء") },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VibrantPurple,
            unfocusedBorderColor = VibrantDarkSurfaceVariant,
            focusedTextColor = VibrantTextPrimary,
            unfocusedTextColor = VibrantTextPrimary
          ),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("playlist_name_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("الوصف (اختياري)") },
          placeholder = { Text("وصف موجز لمحتوى القائمة") },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VibrantPurple,
            unfocusedBorderColor = VibrantDarkSurfaceVariant,
            focusedTextColor = VibrantTextPrimary,
            unfocusedTextColor = VibrantTextPrimary
          ),
          maxLines = 2,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("playlist_desc_input")
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.isNotBlank()) {
            onSave(name.trim(), description.trim(), selectedEmoji)
          }
        },
        enabled = name.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("save_playlist_button")
      ) {
        Text("حفظ", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("إلغاء", color = VibrantTextMuted)
      }
    }
  )
}
