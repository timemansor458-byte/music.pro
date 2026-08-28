package com.example.model

data class LyricLine(
  val timeMs: Long,
  val text: String,
  val translation: String? = null
)

data class Song(
  val id: String,
  val title: String,
  val artist: String,
  val album: String,
  val durationMs: Long,
  val genre: String,
  val releaseYear: String = "2024",
  val isFavorite: Boolean = false,
  val playCount: Int = 0,
  val lyrics: List<LyricLine> = emptyList(),
  val baseFrequencies: List<Float> = listOf(261.63f, 329.63f, 392.00f, 523.25f), // C4, E4, G4, C5
  val tempoBpm: Int = 110
) {
  val formattedDuration: String
    get() {
      val totalSeconds = durationMs / 1000
      val minutes = totalSeconds / 60
      val seconds = totalSeconds % 60
      return String.format("%02d:%02d", minutes, seconds)
    }
}

data class Playlist(
  val id: String,
  val name: String,
  val description: String,
  val songIds: List<String>,
  val iconEmoji: String = "🎵",
  val isCustom: Boolean = false,
  val updatedAt: Long = System.currentTimeMillis()
)

data class SyncDevice(
  val id: String,
  val name: String,
  val type: String, // "phone", "tablet", "web", "desktop"
  val isCurrent: Boolean = false,
  val lastActiveTime: String = "الآن"
)

data class UserProfile(
  val id: String = "usr_94812",
  val name: String = "تيم التوت",
  val email: String = "taymaatote435@gmail.com",
  val avatarEmoji: String = "🎧",
  val isLoggedIn: Boolean = true,
  val planName: String = "اشتراك بريميوم (VIP)",
  val connectedDevices: List<SyncDevice> = listOf(
    SyncDevice("dev_1", "هاتف Galaxy S24 Ultra", "phone", isCurrent = true, lastActiveTime = "نشط الآن"),
    SyncDevice("dev_2", "جهاز لوحي Galaxy Tab S9", "tablet", isCurrent = false, lastActiveTime = "منذ 10 دقائق"),
    SyncDevice("dev_3", "متصفح الويب (MacBook Pro)", "desktop", isCurrent = false, lastActiveTime = "أمس")
  ),
  val isSyncing: Boolean = false,
  val lastSyncTime: String = "منذ لحظات",
  val autoSyncEnabled: Boolean = true
)

data class ListeningHistoryItem(
  val songId: String,
  val timestampMs: Long,
  val listenDurationSec: Long = 0
)

data class GenreStats(
  val genre: String,
  val percentage: Float,
  val playCount: Int
)

data class ArtistRecommendation(
  val artistName: String,
  val genre: String,
  val reason: String,
  val matchPercentage: Int,
  val topSongTitle: String,
  val avatarEmoji: String = "🎤"
)

data class RecommendedSongGroup(
  val title: String,
  val subtitle: String,
  val iconEmoji: String,
  val songs: List<Song>
)

enum class RepeatMode {
  OFF,
  ALL,
  ONE
}

enum class NavigationTab {
  HOME,
  RECOMMENDATIONS,
  DOWNLOADER,
  SEARCH,
  LIBRARY,
  EQUALIZER
}

