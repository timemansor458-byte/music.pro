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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import com.example.model.Playlist
import com.example.model.Song
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun PlaylistSelectorDialog(
  song: Song,
  playlists: List<Playlist>,
  onAddToPlaylist: (String, String) -> Unit,
  onCreateAndAdd: (String, String) -> Unit,
  onDismiss: () -> Unit
) {
  var showNewPlaylistInput by remember { mutableStateOf(false) }
  var newPlaylistTitle by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .testTag("playlist_selector_dialog"),
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
              imageVector = Icons.Default.PlaylistAdd,
              contentDescription = null,
              tint = VibrantPurple
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "إضافة إلى قائمة التشغيل",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = VibrantTextPrimary
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = VibrantTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "اختر القائمة لإضافة \"${song.title}\":",
          color = VibrantTextSecondary,
          style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (showNewPlaylistInput) {
          OutlinedTextField(
            value = newPlaylistTitle,
            onValueChange = { newPlaylistTitle = it },
            placeholder = { Text("اسم القائمة الجديدة...", color = VibrantTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VibrantPurple,
              unfocusedBorderColor = VibrantDarkActiveSurface,
              focusedTextColor = VibrantTextPrimary,
              unfocusedTextColor = VibrantTextPrimary
            ),
            shape = RoundedCornerShape(14.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Button(
              onClick = {
                if (newPlaylistTitle.isNotBlank()) {
                  onCreateAndAdd(newPlaylistTitle, song.id)
                  onDismiss()
                }
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = VibrantPurple,
                contentColor = VibrantPurpleContainer
              ),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("إنشاء وإضافة", fontWeight = FontWeight.Bold)
            }
          }
        } else {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(VibrantDarkActiveSurface)
              .clickable { showNewPlaylistInput = true }
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(VibrantPurple.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Add, contentDescription = null, tint = VibrantPurple)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "إنشاء قائمة تشغيل جديدة",
              color = VibrantPurple,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          LazyColumn(
            modifier = Modifier.height(200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(playlists) { playlist ->
              val alreadyInPlaylist = playlist.songIds.contains(song.id)
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(14.dp))
                  .background(if (alreadyInPlaylist) VibrantPurple.copy(alpha = 0.1f) else VibrantDarkActiveSurface)
                  .clickable {
                    if (!alreadyInPlaylist) {
                      onAddToPlaylist(playlist.id, song.id)
                      onDismiss()
                    }
                  }
                  .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = playlist.iconEmoji, fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(
                      text = playlist.name,
                      color = VibrantTextPrimary,
                      fontWeight = FontWeight.Medium
                    )
                    Text(
                      text = "${playlist.songIds.size} أغاني",
                      color = VibrantTextMuted,
                      style = MaterialTheme.typography.bodySmall
                    )
                  }
                }

                if (alreadyInPlaylist) {
                  Icon(Icons.Default.Check, contentDescription = "موجودة مسبقاً", tint = VibrantPurple)
                }
              }
            }
          }
        }
      }
    }
  }
}
