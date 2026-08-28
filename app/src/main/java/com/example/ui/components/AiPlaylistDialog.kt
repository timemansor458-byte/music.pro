package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantPurpleDark
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiPlaylistDialog(
  isGenerating: Boolean,
  onGenerate: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var moodPrompt by remember { mutableStateOf("") }
  val suggestedPrompts = listOf(
    "طرب وسلطنة أصيلة",
    "جلسة هادئة وتركيز للدراسة",
    "حماس وطاقة للرياضة والجيم",
    "رومانسيات وذكريات",
    "أغاني طريق وسفر مسائي"
  )

  Dialog(onDismissRequest = { if (!isGenerating) onDismiss() }) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .testTag("ai_playlist_dialog"),
      colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
      shape = RoundedCornerShape(28.dp)
    ) {
      Column(
        modifier = Modifier.padding(24.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = VibrantPurple
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "توليد قائمة ذكية (AI)",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              ),
              color = VibrantTextPrimary
            )
          }
          if (!isGenerating) {
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = VibrantTextMuted)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "صف حالتك المزاجية أو النشاط وسيقوم الذكاء الاصطناعي بتركيب قائمة أغاني مثالية لك:",
          color = VibrantTextSecondary,
          style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = moodPrompt,
          onValueChange = { moodPrompt = it },
          placeholder = { Text("مثال: طرب راقي وهادئ مع فنجان قهوة...", color = VibrantTextMuted) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("ai_mood_input"),
          enabled = !isGenerating,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VibrantPurple,
            unfocusedBorderColor = VibrantDarkActiveSurface,
            focusedTextColor = VibrantTextPrimary,
            unfocusedTextColor = VibrantTextPrimary,
            cursorColor = VibrantPurple
          ),
          shape = RoundedCornerShape(16.dp),
          maxLines = 2
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "اقتراحات سريعة:",
          color = VibrantTextMuted,
          style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          suggestedPrompts.forEach { prompt ->
            SuggestionChip(
              onClick = { moodPrompt = prompt },
              label = { Text(prompt, fontSize = 12.sp, color = VibrantTextSecondary) },
              colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor = VibrantDarkActiveSurface
              ),
              border = null,
              shape = RoundedCornerShape(12.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = {
            if (moodPrompt.isNotBlank()) {
              onGenerate(moodPrompt)
              onDismiss()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("generate_ai_playlist_button"),
          enabled = !isGenerating && moodPrompt.isNotBlank(),
          colors = ButtonDefaults.buttonColors(
            containerColor = VibrantPurple,
            contentColor = VibrantPurpleContainer
          ),
          shape = RoundedCornerShape(16.dp)
        ) {
          if (isGenerating) {
            CircularProgressIndicator(
              modifier = Modifier.size(22.dp),
              color = VibrantPurpleContainer,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("جاري توليد القائمة...", fontWeight = FontWeight.Bold)
          } else {
            Icon(Icons.Default.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("توليد القائمة الآن", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
