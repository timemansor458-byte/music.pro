package com.example.model

data class EqualizerBand(
  val label: String,
  val frequencyHz: Int,
  val gainDb: Float = 0f // -12f to +12f
)

data class EqualizerState(
  val isEnabled: Boolean = true,
  val selectedPreset: String = "افتراضي (Flat)",
  val bands: List<EqualizerBand> = defaultBands,
  val bassBoost: Float = 40f, // 0 to 100
  val virtualizer3d: Float = 50f, // 0 to 100
  val loudnessEnhancer: Float = 30f // 0 to 100
) {
  companion object {
    val defaultBands = listOf(
      EqualizerBand("60 Hz", 60, 0f),
      EqualizerBand("230 Hz", 230, 2f),
      EqualizerBand("910 Hz", 910, 0f),
      EqualizerBand("3.6 kHz", 3600, 3f),
      EqualizerBand("14 kHz", 14000, 4f)
    )

    val presets = mapOf(
      "افتراضي (Flat)" to listOf(0f, 0f, 0f, 0f, 0f),
      "مضخم الصوت الجهير (Bass Boost)" to listOf(8f, 5f, 0f, 0f, 1f),
      "طرب وأصوات شرقية (Vocal Tarab)" to listOf(2f, 4f, 7f, 6f, 3f),
      "بوب عصري (Pop)" to listOf(2f, 4f, 6f, 3f, 2f),
      "روك وحماس (Rock)" to listOf(5f, 3f, -1f, 4f, 6f),
      "كلاسيك وهادئ (Classical)" to listOf(4f, 3f, 2f, 3f, 4f),
      "جاز وسلس (Jazz)" to listOf(3f, 2f, 1f, 3f, 5f),
      "أجواء سينمائية (Cinema 3D)" to listOf(6f, 2f, 3f, 5f, 7f)
    )
  }
}
