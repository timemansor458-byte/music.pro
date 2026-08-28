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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleMusicData
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
fun SearchScreen(
  songs: List<Song>,
  searchQuery: String,
  selectedGenre: String,
  currentPlayingSong: Song?,
  onQueryChange: (String) -> Unit,
  onGenreSelect: (String) -> Unit,
  onSongClick: (Song) -> Unit,
  onToggleFavorite: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val filteredSongs = songs.filter { song ->
    val matchesQuery = searchQuery.isBlank() ||
      song.title.contains(searchQuery, ignoreCase = true) ||
      song.artist.contains(searchQuery, ignoreCase = true) ||
      song.album.contains(searchQuery, ignoreCase = true) ||
      song.genre.contains(searchQuery, ignoreCase = true)

    val matchesGenre = selectedGenre == "الكل" || song.genre == selectedGenre

    matchesQuery && matchesGenre
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .statusBarsPadding()
      .padding(horizontal = 20.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "البحث والاستكشاف",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = VibrantTextPrimary
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onQueryChange,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("search_text_field"),
      placeholder = { Text("ابحث عن أغنية، فنان، ألبوم أو نوع...", color = VibrantTextMuted) },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = VibrantPurple) },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { onQueryChange("") }) {
            Icon(Icons.Default.Clear, contentDescription = "مسح", tint = VibrantTextMuted)
          }
        }
      },
      shape = RoundedCornerShape(20.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VibrantPurple,
        unfocusedBorderColor = VibrantDarkSurface,
        focusedContainerColor = VibrantDarkSurface,
        unfocusedContainerColor = VibrantDarkSurface,
        focusedTextColor = VibrantTextPrimary,
        unfocusedTextColor = VibrantTextPrimary,
        cursorColor = VibrantPurple
      ),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Genre Filter Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(SampleMusicData.genres) { genre ->
        val isSelected = selectedGenre == genre
        FilterChip(
          selected = isSelected,
          onClick = { onGenreSelect(genre) },
          label = { Text(genre, fontSize = 13.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = VibrantPurple,
            selectedLabelColor = VibrantPurpleContainer,
            containerColor = VibrantDarkSurface,
            labelColor = VibrantTextSecondary
          ),
          border = null,
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.testTag("genre_chip_$genre")
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Results Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "النتائج (${filteredSongs.size})",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = VibrantTextPrimary
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (filteredSongs.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            tint = VibrantTextMuted,
            modifier = Modifier.size(56.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "لا توجد أغانٍ مطابقة لبحثك",
            color = VibrantTextMuted,
            style = MaterialTheme.typography.bodyLarge
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        items(filteredSongs) { song ->
          val isCurrent = currentPlayingSong?.id == song.id
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(if (isCurrent) VibrantDarkActiveSurface else Color.Transparent)
              .clickable { onSongClick(song) }
              .padding(horizontal = 12.dp, vertical = 10.dp)
              .testTag("search_song_item_${song.id}"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isCurrent) VibrantPurple else VibrantDarkSurface),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "♪",
                fontSize = 20.sp,
                color = if (isCurrent) VibrantPurpleContainer else VibrantPurple
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = song.title,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isCurrent) VibrantPurple else VibrantTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "${song.artist} • ${song.genre}",
                style = MaterialTheme.typography.bodySmall,
                color = VibrantTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

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
          }
        }
      }
    }
  }
}
