package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SampleMusicData
import com.example.model.ListeningHistoryItem
import com.example.service.RecommendationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Vibrant Music", appName)
  }

  @Test
  fun `recommendation engine generates valid genre stats and recommendations`() {
    val songs = SampleMusicData.songs
    val history = listOf(
      ListeningHistoryItem("song_1", System.currentTimeMillis(), 250),
      ListeningHistoryItem("song_2", System.currentTimeMillis(), 200)
    )

    val genreStats = RecommendationEngine.calculateGenreStats(songs, history)
    assertTrue("Genre stats should not be empty", genreStats.isNotEmpty())

    val artistRecs = RecommendationEngine.getArtistRecommendations(songs, history)
    assertTrue("Artist recommendations should not be empty", artistRecs.isNotEmpty())

    val recGroups = RecommendationEngine.generateRecommendations(songs, history)
    assertTrue("Recommended song groups should not be empty", recGroups.isNotEmpty())
  }

  @Test
  fun `earbuds state and noise control modes work correctly`() {
    val earbuds = com.example.model.EarbudsState(
      leftBattery = 95,
      rightBattery = 90,
      caseBattery = 80,
      noiseControlMode = com.example.model.NoiseControlMode.NOISE_CANCELLATION
    )
    assertEquals(95, earbuds.leftBattery)
    assertEquals(90, earbuds.rightBattery)
    assertEquals(80, earbuds.caseBattery)
    assertEquals("إلغاء الضوضاء", earbuds.noiseControlMode.labelAr)
  }

  @Test
  fun `youtube mp3 downloader audio qualities are strictly MP3 only`() {
    val qualities = com.example.model.Mp3AudioQuality.values()
    assertTrue(qualities.isNotEmpty())
    qualities.forEach { quality ->
      assertTrue(quality.formatDescription.contains("MP3"))
    }
  }
}


