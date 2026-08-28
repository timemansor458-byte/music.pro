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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ArtistRecommendation
import com.example.model.RecommendedSongGroup
import com.example.model.Song
import com.example.ui.theme.VibrantAccentAmber
import com.example.ui.theme.VibrantAccentCyan
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
fun RecommendationsScreen(
  artistRecommendations: List<ArtistRecommendation>,
  recommendedGroups: List<RecommendedSongGroup>,
  currentPlayingSong: Song?,
  isPlaying: Boolean,
  onPlaySong: (Song) -> Unit,
  onToggleFavorite: (String) -> Unit,
  onOpenStats: () -> Unit,
  onRefreshRecommendations: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(VibrantDarkBackground)
      .padding(horizontal = 16.dp)
      .testTag("recommendations_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "توصيات ذكية مخصصة",
            color = VibrantTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
          )
          Text(
            text = "تحليل فوري لسجل استماعك وأنماطك الموسيقية",
            color = VibrantTextMuted,
            fontSize = 13.sp
          )
        }
        IconButton(
          onClick = onRefreshRecommendations,
          modifier = Modifier.testTag("refresh_recs_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "تحديث التوصيات",
            tint = VibrantPurple
          )
        }
      }
    }

    // Recommendation AI Radar Banner
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.horizontalGradient(
                listOf(
                  VibrantPurpleContainer.copy(alpha = 0.6f),
                  VibrantDarkSurface
                )
              )
            )
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = VibrantPurple,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "رادار الذكاء الموسيقي",
                  color = VibrantPurple,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "اكتشف إحصائيات استماعك ونسب تفضيل الأنواع",
                color = VibrantTextPrimary,
                fontSize = 12.sp
              )
            }

            Button(
              onClick = onOpenStats,
              colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("open_taste_stats_btn")
            ) {
              Icon(Icons.Default.Analytics, contentDescription = null, tint = VibrantDarkBackground, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("عرض التحليل", color = VibrantDarkBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Artist Recommendations Carousel (Artist Radar)
    item {
      Column {
        Text(
          text = "فنانون مقترحون لذوقك",
          color = VibrantTextPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
        Text(
          text = "بناءً على الأغاني الأكثر تكراراً في تاريخ استماعك",
          color = VibrantTextMuted,
          fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(artistRecommendations) { artist ->
            ArtistRecommendationCard(
              artist = artist,
              onPlayTopSong = {
                // Play matching song
              }
            )
          }
        }
      }
    }

    // Dynamic Song Groups
    items(recommendedGroups) { group ->
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = group.iconEmoji, fontSize = 18.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = group.title,
              color = VibrantTextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
            Text(
              text = group.subtitle,
              color = VibrantTextMuted,
              fontSize = 11.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        group.songs.forEach { song ->
          val isCurrent = currentPlayingSong?.id == song.id
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isCurrent) VibrantDarkActiveSurface else VibrantDarkSurface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clickable { onPlaySong(song) }
              .testTag("rec_song_item_${song.id}")
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
                  .background(if (isCurrent) VibrantPurpleContainer else VibrantDarkSurfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                if (isCurrent && isPlaying) {
                  Text("▶", color = VibrantPurple, fontSize = 12.sp)
                } else {
                  Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isCurrent) VibrantPurple else VibrantTextSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = song.title,
                  color = if (isCurrent) VibrantPurple else VibrantTextPrimary,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp,
                  maxLines = 1
                )
                Text(
                  text = "${song.artist} • ${song.genre}",
                  color = VibrantTextMuted,
                  fontSize = 11.sp,
                  maxLines = 1
                )
              }

              IconButton(onClick = { onToggleFavorite(song.id) }) {
                Icon(
                  imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                  contentDescription = "تفضيل",
                  tint = if (song.isFavorite) VibrantAccentPink else VibrantTextMuted,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
private fun ArtistRecommendationCard(
  artist: ArtistRecommendation,
  onPlayTopSong: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = VibrantDarkSurface),
    modifier = Modifier
      .width(170.dp)
      .testTag("artist_card_${artist.artistName}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(VibrantPurpleContainer),
        contentAlignment = Alignment.Center
      ) {
        Text(text = artist.avatarEmoji, fontSize = 26.sp)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = artist.artistName,
        color = VibrantTextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        maxLines = 1
      )

      Text(
        text = artist.genre,
        color = VibrantPurple,
        fontSize = 11.sp
      )

      Spacer(modifier = Modifier.height(4.dp))

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(VibrantAccentCyan.copy(alpha = 0.15f))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = "${artist.matchPercentage}% تطابق",
          color = VibrantAccentCyan,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = artist.reason,
        color = VibrantTextMuted,
        fontSize = 10.sp,
        maxLines = 2,
        lineHeight = 13.sp
      )
    }
  }
}
