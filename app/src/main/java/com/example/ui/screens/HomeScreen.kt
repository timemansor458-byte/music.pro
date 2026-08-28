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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EarbudsState
import com.example.model.Playlist
import com.example.model.Song
import com.example.model.UserProfile
import com.example.ui.theme.VibrantAccentGreen
import com.example.ui.theme.VibrantAccentPink
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantPurpleDark
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun HomeScreen(
  songs: List<Song>,
  playlists: List<Playlist>,
  currentPlayingSong: Song?,
  isPlaying: Boolean,
  userProfile: UserProfile,
  earbudsState: EarbudsState,
  onSongClick: (Song) -> Unit,
  onPlaylistClick: (Playlist) -> Unit,
  onToggleFavorite: (String) -> Unit,
  onOpenAiDialog: () -> Unit,
  onOpenSyncProfile: () -> Unit,
  onOpenEarbudsPopup: () -> Unit,
  onOpenAddToPlaylist: (Song) -> Unit,
  modifier: Modifier = Modifier
) {
  val featuredSong = songs.firstOrNull() ?: return

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .statusBarsPadding()
      .padding(horizontal = 20.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)
  ) {
    // Top Bar Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "مرحباً بك ${userProfile.name} 🎵",
            style = MaterialTheme.typography.bodyMedium,
            color = VibrantTextSecondary
          )
          Text(
            text = "أنغام الموسيقى",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = VibrantTextPrimary
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // AirPods / Earbuds Connection Pill Button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(VibrantDarkActiveSurface)
              .clickable(onClick = onOpenEarbudsPopup)
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("home_earbuds_btn"),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = "🎧", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${earbudsState.leftBattery}%",
                color = VibrantAccentGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }
          }

          // Cloud Sync & Profile Button
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(VibrantDarkActiveSurface)
              .clickable(onClick = onOpenSyncProfile)
              .testTag("home_sync_profile_btn"),
            contentAlignment = Alignment.Center
          ) {
            Text(text = userProfile.avatarEmoji, fontSize = 18.sp)
          }

          // AI Dialog Button
          IconButton(
            onClick = onOpenAiDialog,
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(VibrantPurpleContainer)
              .testTag("home_ai_button")
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "الذكاء الاصطناعي",
              tint = VibrantPurple
            )
          }
        }
      }
    }

    // Hero Banner: Featured Song of the Day ("سر الوجود")
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(32.dp))
          .clickable { onSongClick(featuredSong) }
          .testTag("featured_hero_card"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  VibrantPurpleDark,
                  VibrantPurpleContainer,
                  VibrantDarkSurface
                )
              )
            )
            .padding(24.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(VibrantPurple.copy(alpha = 0.25f))
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "أغنية مميزة اليوم",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = VibrantPurple
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = featuredSong.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantTextPrimary
              )

              Text(
                text = "${featuredSong.artist} • ${featuredSong.genre}",
                fontSize = 15.sp,
                color = VibrantTextSecondary
              )
            }

            // Big Play Button
            Box(
              modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(VibrantPurple)
                .shadow(8.dp, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "تشغيل",
                tint = VibrantPurpleContainer,
                modifier = Modifier.size(32.dp)
              )
            }
          }
        }
      }
    }

    // AI Smart Mood Card
    item {
      Spacer(modifier = Modifier.height(20.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .clickable(onClick = onOpenAiDialog)
          .testTag("ai_mood_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = VibrantDarkActiveSurface)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(VibrantPurple.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = VibrantPurple,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = "قائمة ذكية حسب مزاجك",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = VibrantTextPrimary
              )
              Text(
                text = "اختر الحالة واصنع قائمتك بلمسة ذكاء اصطناعي",
                style = MaterialTheme.typography.bodySmall,
                color = VibrantTextMuted
              )
            }
          }
        }
      }
    }

    // Featured Playlists Carousel
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = "قوائم التشغيل المختارة",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = VibrantTextPrimary,
        modifier = Modifier.padding(bottom = 12.dp)
      )

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(playlists) { playlist ->
          Card(
            modifier = Modifier
              .width(150.dp)
              .clip(RoundedCornerShape(24.dp))
              .clickable { onPlaylistClick(playlist) }
              .testTag("playlist_card_${playlist.id}"),
            colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
            shape = RoundedCornerShape(24.dp)
          ) {
            Column(
              modifier = Modifier.padding(14.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(122.dp)
                  .clip(RoundedCornerShape(18.dp))
                  .background(
                    Brush.verticalGradient(
                      colors = listOf(VibrantPurpleDark.copy(alpha = 0.8f), VibrantPurpleContainer)
                    )
                  ),
                contentAlignment = Alignment.Center
              ) {
                Text(text = playlist.iconEmoji, fontSize = 42.sp)
              }
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = playlist.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = VibrantTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "${playlist.songIds.size} أغاني",
                style = MaterialTheme.typography.bodySmall,
                color = VibrantTextMuted
              )
            }
          }
        }
      }
    }

    // All Songs List Section
    item {
      Spacer(modifier = Modifier.height(28.dp))
      Text(
        text = "أغاني مختارة لك",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = VibrantTextPrimary,
        modifier = Modifier.padding(bottom = 12.dp)
      )
    }

    items(songs) { song ->
      val isThisSongPlaying = currentPlayingSong?.id == song.id && isPlaying
      var showMenu by remember { mutableStateOf(false) }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clip(RoundedCornerShape(18.dp))
          .background(if (currentPlayingSong?.id == song.id) VibrantDarkActiveSurface else Color.Transparent)
          .clickable { onSongClick(song) }
          .padding(horizontal = 12.dp, vertical = 10.dp)
          .testTag("song_item_${song.id}"),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Song Icon / Play indicator
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (currentPlayingSong?.id == song.id) VibrantPurple else VibrantDarkSurface),
          contentAlignment = Alignment.Center
        ) {
          if (isThisSongPlaying) {
            Icon(Icons.Default.Equalizer, contentDescription = null, tint = VibrantPurpleContainer)
          } else {
            Text(
              text = when {
                song.genre.contains("طرب") -> "🎻"
                song.genre.contains("عود") -> "🪕"
                song.genre.contains("بوب") -> "⚡"
                song.genre.contains("هادئة") -> "🌙"
                else -> "♪"
              },
              fontSize = 20.sp,
              color = if (currentPlayingSong?.id == song.id) VibrantPurpleContainer else VibrantTextPrimary
            )
          }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Title and artist
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = song.title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = if (currentPlayingSong?.id == song.id) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (currentPlayingSong?.id == song.id) VibrantPurple else VibrantTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "${song.artist} • ${song.formattedDuration}",
            style = MaterialTheme.typography.bodySmall,
            color = VibrantTextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        // Favorite Toggle
        IconButton(
          onClick = { onToggleFavorite(song.id) },
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "المفضلة",
            tint = if (song.isFavorite) VibrantAccentPink else VibrantTextMuted,
            modifier = Modifier.size(20.dp)
          )
        }

        // Menu options
        Box {
          IconButton(
            onClick = { showMenu = true },
            modifier = Modifier.size(36.dp)
          ) {
            Icon(Icons.Default.MoreVert, contentDescription = "خيارات", tint = VibrantTextMuted)
          }

          DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier.background(VibrantDarkActiveSurface)
          ) {
            DropdownMenuItem(
              text = { Text("إضافة إلى قائمة التشغيل", color = VibrantTextPrimary) },
              onClick = {
                showMenu = false
                onOpenAddToPlaylist(song)
              }
            )
          }
        }
      }
    }
  }
}
