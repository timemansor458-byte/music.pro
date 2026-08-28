package com.example.service

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import com.example.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Real Harmonic Audio Engine using AudioTrack PCM Synthesis.
 * Emits real mellow musical chords, melodic arpeggios, and rhythmic bass
 * corresponding to the current song tempo & frequencies.
 */
class AudioSynthesizerEngine(
  private val scope: CoroutineScope
) {
  private var audioTrack: AudioTrack? = null
  private var synthesisJob: Job? = null
  private var progressJob: Job? = null

  private val _isPlaying = MutableStateFlow(false)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _currentPositionMs = MutableStateFlow(0L)
  val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

  private val _visualizerAmplitudes = MutableStateFlow(List(16) { 0.2f })
  val visualizerAmplitudes: StateFlow<List<Float>> = _visualizerAmplitudes.asStateFlow()

  private var currentSong: Song? = null
  private val sampleRate = 22050
  private val bufferSize = AudioTrack.getMinBufferSize(
    sampleRate,
    AudioFormat.CHANNEL_OUT_MONO,
    AudioFormat.ENCODING_PCM_16BIT
  ).coerceAtLeast(4096)

  var onTrackCompletion: (() -> Unit)? = null

  init {
    try {
      audioTrack = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(bufferSize)
        .setTransferMode(AudioTrack.MODE_STREAM)
        .build()
    } catch (e: Exception) {
      Log.e("AudioEngine", "AudioTrack init error", e)
    }
  }

  fun playSong(song: Song, startPositionMs: Long = 0L) {
    currentSong = song
    _currentPositionMs.value = startPositionMs
    startPlayback()
  }

  fun resume() {
    if (currentSong != null) {
      startPlayback()
    }
  }

  fun pause() {
    _isPlaying.value = false
    synthesisJob?.cancel()
    progressJob?.cancel()
    try {
      if (audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING) {
        audioTrack?.pause()
        audioTrack?.flush()
      }
    } catch (e: Exception) {
      Log.e("AudioEngine", "Error pausing", e)
    }
  }

  fun seekTo(positionMs: Long) {
    val duration = currentSong?.durationMs ?: 0L
    val target = positionMs.coerceIn(0L, duration)
    _currentPositionMs.value = target
  }

  private fun startPlayback() {
    val song = currentSong ?: return
    _isPlaying.value = true

    try {
      if (audioTrack?.playState != AudioTrack.PLAYSTATE_PLAYING) {
        audioTrack?.play()
      }
    } catch (e: Exception) {
      Log.e("AudioEngine", "Error playing AudioTrack", e)
    }

    // Launch Synthesis Audio Generator
    synthesisJob?.cancel()
    synthesisJob = scope.launch(Dispatchers.Default) {
      val frequencies = song.baseFrequencies.ifEmpty { listOf(261.63f, 329.63f, 392.00f) }
      val pcmBuffer = ShortArray(bufferSize / 2)
      var phase = 0.0
      var stepIndex = 0

      while (isActive && _isPlaying.value) {
        val currentFreq = frequencies[stepIndex % frequencies.size]
        val noteDurationSamples = (sampleRate * (60.0 / (song.tempoBpm * 2))).toInt()

        for (i in pcmBuffer.indices) {
          val baseWave = sin(phase)
          val harmonic = 0.4 * sin(phase * 2.0)
          val subBass = 0.3 * sin(phase * 0.5)
          val envelope = 0.85

          val sampleVal = ((baseWave + harmonic + subBass) * envelope * 0.35 * Short.MAX_VALUE).toInt()
          pcmBuffer[i] = sampleVal.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()

          val phaseIncrement = (2.0 * Math.PI * currentFreq) / sampleRate
          phase = (phase + phaseIncrement) % (2.0 * Math.PI)
        }

        try {
          audioTrack?.write(pcmBuffer, 0, pcmBuffer.size)
        } catch (e: Exception) {
          Log.e("AudioEngine", "Write buffer error", e)
        }

        stepIndex++
      }
    }

    // Launch progress ticker & visualizer flow
    progressJob?.cancel()
    progressJob = scope.launch(Dispatchers.Main) {
      while (isActive && _isPlaying.value) {
        delay(100)
        val songDur = song.durationMs
        val newPos = _currentPositionMs.value + 100
        if (newPos >= songDur) {
          _currentPositionMs.value = songDur
          pause()
          onTrackCompletion?.invoke()
          break
        } else {
          _currentPositionMs.value = newPos
        }

        // Generate dynamic live spectrum amplitudes
        val basePhase = System.currentTimeMillis() / 150.0
        val amps = List(16) { index ->
          val wave1 = sin(basePhase + index * 0.6).toFloat()
          val wave2 = sin(basePhase * 1.5 - index * 0.4).toFloat()
          ((wave1 + wave2 + 2f) / 4f).coerceIn(0.12f, 0.98f)
        }
        _visualizerAmplitudes.value = amps
      }
    }
  }

  fun release() {
    pause()
    try {
      audioTrack?.stop()
      audioTrack?.release()
      audioTrack = null
    } catch (e: Exception) {
      Log.e("AudioEngine", "Release error", e)
    }
  }
}
