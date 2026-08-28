package com.example.model

enum class NoiseControlMode(val labelAr: String) {
  NOISE_CANCELLATION("إلغاء الضوضاء"),
  OFF("متوقف"),
  TRANSPARENCY("شفافية الصوت")
}

data class EarbudsState(
  val deviceName: String = "AirPods Pro (تيم)",
  val isConnected: Boolean = true,
  val isCaseOpen: Boolean = false,
  val leftBattery: Int = 96,
  val isLeftCharging: Boolean = false,
  val rightBattery: Int = 94,
  val isRightCharging: Boolean = true,
  val caseBattery: Int = 82,
  val isCaseCharging: Boolean = false,
  val isSpatialAudioActive: Boolean = true,
  val noiseControlMode: NoiseControlMode = NoiseControlMode.NOISE_CANCELLATION
)
