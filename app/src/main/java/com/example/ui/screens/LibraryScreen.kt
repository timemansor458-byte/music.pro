package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Playlist
import com.example.model.Song
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantNavPill
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun LibraryScreen(
  playlists: List<Playlist>,
  songs: List<Song>,
  onPlaylistClick: (Playlist) -> Unit,
  onSongClick: (Song) -> Unit,
  onCreatePlaylist: (String) -> Unit,
  onToggleFavorite: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var showCreateDialog by remember { mutableStateOf(false) }
  var newPlaylistName by remember { mutableStateOf("") }

  val tabs = listOf("قوائم التشغيل", "الأغاني المفضلة", "الفنانون")
  val favoriteSongs = songs.filter { it.isFavorite }
  val uniqueArtists = songs.map { it.artist }.distinct()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .statusBarsPadding()
      .padding(horizontal = 20.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "مكتبتي الموسيقية",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = VibrantTextPrimary
      )

      Button(
        onClick = { showCreateDialog = true },
        colors = ButtonDefaults.buttonColors(
          containerColor = VibrantPurple,
          contentColor = VibrantPurpleContainer
        ),
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        modifier = Modifier.testTag("create_playlist_button")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("قائمة جديدة", fontSize = 13.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Tab Row
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = VibrantDarkSurface,
      contentColor = VibrantPurple,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = VibrantPurple,
          height = 3.dp
        )
      },
      modifier = Modifier.clip(RoundedCornerShape(16.dp))
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              color = if (selectedTab == index) VibrantPurple else VibrantTextSecondary,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
              fontSize = 14.sp
            )
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (selectedTab) {
      0 -> { // Playlists tab
        LazyColumn(
          contentPadding = PaddingValues(bottom = 120.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(playlists) { playlist ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onPlaylistClick(playlist) }
                .testTag("library_playlist_${playlist.id}"),
              colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
              shape = RoundedCornerShape(20.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(52.dp)
                      .clip(RoundedCornerShape(14.dp))
                      .background(VibrantDarkActiveSurface),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(text = playlist.iconEmoji, fontSize = 26.sp)
                  }
                  Spacer(modifier = Modifier.width(14.dp))
                  Column {
                    Text(
                      text = playlist.name,
                      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                      color = VibrantTextPrimary
                    )
                    Text(
                      text = "${playlist.songIds.size} أغاني • ${playlist.description}",
                      style = MaterialTheme.typography.bodySmall,
                      color = VibrantTextMuted,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }

                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VibrantPurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "تشغيل القائمة",
                    tint = VibrantPurple,
                    modifier = Modifier.size(24.dp)
                  )
                }
              }
            }
          }
        }
      }

      1 -> { // Favorites tab
        if (favoriteSongs.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "لم تقم بإضافة أغانٍ للمفضلة بعد",
              color = VibrantTextMuted,
              style = MaterialTheme.typography.bodyLarge
            )
          }
        } else {
          LazyColumn(
            contentPadding = PaddingValues(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            items(favoriteSongs) { song ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(VibrantDarkSurface)
                  .clickable { onSongClick(song) }
                  .padding(horizontal = 14.dp, vertical = 12.dp)
                  .testTag("fav_song_${song.id}"),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VibrantPurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = "♪", color = VibrantPurple, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = song.title,
                    color = VibrantTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  )
                  Text(
                    text = "${song.artist} • ${song.formattedDuration}",
                    color = VibrantTextMuted,
                    style = MaterialTheme.typography.bodySmall
                  )
                }
                Icon(
                  imageVector = Icons.Default.Favorite,
                  contentDescription = "مفضلة",
                  tint = VibrantAccentPink,
                  modifier = Modifier
                    .size(24.dp)
                    .clickable { onToggleFavorite(song.id) }
                )
              }
            }
          }
        }
      }

      2 -> { // Artists tab
        LazyColumn(
          contentPadding = PaddingValues(bottom = 120.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(uniqueArtists) { artist ->
            val count = songs.count { it.artist == artist }
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(VibrantPurpleContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = "🎤", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                  Text(
                    text = artist,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = VibrantTextPrimary
                  )
                  Text(
                    text = "$count أعمال في المكتبة",
                    style = MaterialTheme.typography.bodySmall,
                    color = VibrantTextMuted
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Create Playlist Dialog
  if (showCreateDialog) {
    Dialog(onDismissRequest = { showCreateDialog = false }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
        shape = RoundedCornerShape(24.dp)
      ) {
        Column(modifier = Modifier.padding(24.dp)) {
          Text(
            text = "إنشاء قائمة تشغيل جديدة",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = VibrantTextPrimary
          )
          Spacer(modifier = Modifier.height(16.dp))
          OutlinedTextField(
            value = newPlaylistName,
            onValueChange = { newPlaylistName = it },
            placeholder = { Text("اسم القائمة (مثال: أغانٍ للمساء)", color = VibrantTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = VibrantPurple,
              unfocusedBorderColor = VibrantDarkActiveSurface,
              focusedTextColor = VibrantTextPrimary,
              unfocusedTextColor = VibrantTextPrimary
            ),
            shape = RoundedCornerShape(14.dp)
          )
          Spacer(modifier = Modifier.height(20.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Button(
              onClick = {
                if (newPlaylistName.isNotBlank()) {
                  onCreatePlaylist(newPlaylistName)
                  newPlaylistName = ""
                  showCreateDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = VibrantPurple,
                contentColor = VibrantPurpleContainer
              ),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("إنشاء", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
