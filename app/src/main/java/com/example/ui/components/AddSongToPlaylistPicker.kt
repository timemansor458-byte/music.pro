package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.model.Playlist
import com.example.model.Song
import com.example.ui.theme.VibrantAccentCyan
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantDarkSurfaceVariant
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun AddSongToPlaylistPicker(
  playlist: Playlist,
  allSongs: List<Song>,
  onDismiss: () -> Unit,
  onToggleSong: (songId: String, isInPlaylist: Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredSongs = remember(searchQuery, allSongs) {
    if (searchQuery.isBlank()) {
      allSongs
    } else {
      allSongs.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
        it.artist.contains(searchQuery, ignoreCase = true) ||
        it.genre.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("add_song_picker_dialog"),
    shape = RoundedCornerShape(24.dp),
    containerColor = VibrantDarkSurface,
    title = {
      Column {
        Text(
          text = "إضافة أغانٍ إلى: ${playlist.name}",
          color = VibrantTextPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
        Text(
          text = "اضغط على الأغنية لإضافتها أو إزالتها",
          color = VibrantTextMuted,
          fontSize = 12.sp
        )
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .height(380.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("بحث في المكتبة...", color = VibrantTextMuted) },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = VibrantTextMuted)
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VibrantPurple,
            unfocusedBorderColor = VibrantDarkSurfaceVariant,
            focusedTextColor = VibrantTextPrimary,
            unfocusedTextColor = VibrantTextPrimary
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("picker_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(filteredSongs, key = { it.id }) { song ->
            val isInPlaylist = playlist.songIds.contains(song.id)
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isInPlaylist) VibrantDarkActiveSurface else VibrantDarkSurfaceVariant.copy(alpha = 0.4f)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleSong(song.id, isInPlaylist) }
                .testTag("picker_song_item_${song.id}")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isInPlaylist) VibrantPurpleContainer else VibrantDarkSurfaceVariant),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isInPlaylist) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null,
                    tint = if (isInPlaylist) VibrantAccentCyan else VibrantTextMuted,
                    modifier = Modifier.size(18.dp)
                  )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = song.title,
                    color = if (isInPlaylist) VibrantPurple else VibrantTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1
                  )
                  Text(
                    text = "${song.artist} • ${song.formattedDuration}",
                    color = VibrantTextMuted,
                    fontSize = 11.sp,
                    maxLines = 1
                  )
                }

                if (isInPlaylist) {
                  Text(
                    text = "مضافة",
                    color = VibrantAccentCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("done_adding_songs_button")
      ) {
        Text("تم", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
      }
    }
  )
}
