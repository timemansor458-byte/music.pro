package com.example.service

import com.example.model.Playlist
import com.example.model.Song
import com.example.model.SyncDevice
import com.example.model.UserProfile
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CloudSyncService {

  private val dateFormatter = SimpleDateFormat("hh:mm a", Locale("ar"))

  /**
   * Simulates cloud backup and synchronization across devices.
   */
  suspend fun syncWithCloud(
    profile: UserProfile,
    playlists: List<Playlist>,
    favoriteIds: List<String>
  ): SyncResult {
    // Realistic cloud round-trip delay
    delay(1500)

    val updatedDevices = profile.connectedDevices.map { dev ->
      if (dev.isCurrent) {
        dev.copy(lastActiveTime = "متزامن الآن")
      } else {
        dev
      }
    }

    val syncTime = "اليوم ${dateFormatter.format(Date())}"

    return SyncResult(
      success = true,
      syncedPlaylistsCount = playlists.size,
      syncedFavoritesCount = favoriteIds.size,
      lastSyncTimestamp = syncTime,
      updatedDevices = updatedDevices
    )
  }

  data class SyncResult(
    val success: Boolean,
    val syncedPlaylistsCount: Int,
    val syncedFavoritesCount: Int,
    val lastSyncTimestamp: String,
    val updatedDevices: List<SyncDevice>
  )
}
