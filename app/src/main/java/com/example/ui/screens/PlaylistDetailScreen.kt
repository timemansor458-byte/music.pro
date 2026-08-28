package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Playlist
import com.example.model.Song
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantDarkSurfaceVariant
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantPurpleDark
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun PlaylistDetailScreen(
  playlist: Playlist,
  allSongs: List<Song>,
  currentPlayingSong: Song?,
  isPlaying: Boolean,
  onBack: () -> Unit,
  onPlaySong: (Song) -> Unit,
  onPlayPlaylist: (Playlist) -> Unit,
  onToggleFavorite: (String) -> Unit,
  onOpenEditor: () -> Unit,
  onOpenAddSongs: () -> Unit,
  onRemoveSong: (songId: String) -> Unit,
  onMoveUp: (songId: String) -> Unit,
  onMoveDown: (songId: String) -> Unit,
  onDeletePlaylist: (playlistId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showDeleteConfirmation by remember { mutableStateOf(false) }

  val playlistSongs = remember(playlist.songIds, allSongs) {
    playlist.songIds.mapNotNull { id -> allSongs.find { it.id == id } }
  }

  val totalDurationSeconds = playlistSongs.sumOf { it.durationMs } / 1000
  val totalMinutes = totalDurationSeconds / 60

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .testTag("playlist_detail_screen")
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.testTag("playlist_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "الرجوع",
          tint = VibrantTextPrimary
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
          onClick = onOpenEditor,
          modifier = Modifier.testTag("edit_playlist_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "تعديل القائمة",
            tint = VibrantPurple
          )
        }

        if (playlist.isCustom) {
          IconButton(
            onClick = { showDeleteConfirmation = true },
            modifier = Modifier.testTag("delete_playlist_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "حذف القائمة",
              tint = VibrantAccentPink
            )
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Playlist Header Card
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    VibrantPurpleContainer.copy(alpha = 0.5f),
                    VibrantDarkSurface
                  )
                )
              )
              .padding(20.dp)
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(
                modifier = Modifier
                  .size(80.dp)
                  .clip(RoundedCornerShape(20.dp))
                  .background(VibrantPurpleContainer),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = playlist.iconEmoji,
                  fontSize = 42.sp
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = playlist.name,
                color = VibrantTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
              )

              if (playlist.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = playlist.description,
                  color = VibrantTextMuted,
                  fontSize = 13.sp
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = "${playlistSongs.size} أغانٍ • حوالي $totalMinutes دقيقة",
                color = VibrantTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )

              Spacer(modifier = Modifier.height(16.dp))

              // Action Buttons Row
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Button(
                  onClick = { onPlayPlaylist(playlist) },
                  colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
                  shape = RoundedCornerShape(14.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("play_playlist_all_btn")
                ) {
                  Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = VibrantDarkBackground
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    "تشغيل الكل",
                    color = VibrantDarkBackground,
                    fontWeight = FontWeight.Bold
                  )
                }

                Button(
                  onClick = onOpenAddSongs,
                  colors = ButtonDefaults.buttonColors(containerColor = VibrantDarkActiveSurface),
                  shape = RoundedCornerShape(14.dp),
                  modifier = Modifier.testTag("add_songs_to_pl_btn")
                ) {
                  Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = VibrantPurple
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("إضافة أغانٍ", color = VibrantPurple)
                }
              }
            }
          }
        }
      }

      // Songs List Header & Reorder Hint
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "الأغاني والترتيب (${playlistSongs.size})",
            color = VibrantTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
          Text(
            text = "استخدم الأسهم لإعادة الترتيب",
            color = VibrantTextMuted,
            fontSize = 11.sp
          )
        }
      }

      if (playlistSongs.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(text = "🎵", fontSize = 36.sp)
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "القائمة فارغة حالياً",
                color = VibrantTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Text(
                text = "أضف أغانيك المفضلة لتبدأ الاستماع",
                color = VibrantTextMuted,
                fontSize = 13.sp
              )
              Spacer(modifier = Modifier.height(14.dp))
              Button(
                onClick = onOpenAddSongs,
                colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple)
              ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = VibrantDarkBackground)
                Spacer(modifier = Modifier.width(6.dp))
                Text("إضافة أغانٍ الآن", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      } else {
        itemsIndexed(playlistSongs, key = { _, song -> song.id }) { index, song ->
          val isCurrent = currentPlayingSong?.id == song.id

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isCurrent) VibrantDarkActiveSurface else VibrantDarkSurface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onPlaySong(song) }
              .testTag("pl_song_row_${song.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Track Number / Play state indicator
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isCurrent) VibrantPurpleContainer else VibrantDarkSurfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                if (isCurrent && isPlaying) {
                  Text(text = "▶", color = VibrantPurple, fontSize = 12.sp)
                } else {
                  Text(
                    text = "${index + 1}",
                    color = if (isCurrent) VibrantPurple else VibrantTextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Spacer(modifier = Modifier.width(10.dp))

              // Title and Artist
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = song.title,
                  color = if (isCurrent) VibrantPurple else VibrantTextPrimary,
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

              // Reorder Controls (Up & Down)
              Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                  onClick = { onMoveUp(song.id) },
                  enabled = index > 0,
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "تحريك للأعلى",
                    tint = if (index > 0) VibrantTextSecondary else VibrantTextMuted.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = { onMoveDown(song.id) },
                  enabled = index < playlistSongs.size - 1,
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "تحريك للأسفل",
                    tint = if (index < playlistSongs.size - 1) VibrantTextSecondary else VibrantTextMuted.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = { onRemoveSong(song.id) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "إزالة من القائمة",
                    tint = VibrantAccentPink.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Bottom Spacer
      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }

  // Delete Confirmation Dialog
  if (showDeleteConfirmation) {
    AlertDialog(
      onDismissRequest = { showDeleteConfirmation = false },
      containerColor = VibrantDarkSurface,
      shape = RoundedCornerShape(20.dp),
      title = {
        Text("حذف قائمة التشغيل", color = VibrantTextPrimary, fontWeight = FontWeight.Bold)
      },
      text = {
        Text(
          "هل أنت متأكد من رغبتك في حذف قائمة \"${playlist.name}\" نهائياً؟",
          color = VibrantTextMuted
        )
      },
      confirmButton = {
        Button(
          onClick = {
            onDeletePlaylist(playlist.id)
            showDeleteConfirmation = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = VibrantAccentPink)
        ) {
          Text("حذف", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteConfirmation = false }) {
          Text("إلغاء", color = VibrantTextMuted)
        }
      }
    )
  }
}
