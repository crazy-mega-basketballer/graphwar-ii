package com.example.gra.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object SoundManager {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundEffect, Int>()
    private var initialized = false

    enum class SoundEffect {
        SHOOT,
        HIT,
        EXPLOSION,
        MOVE,
        BUTTON_CLICK,
        LEVEL_COMPLETE
    }

    fun initialize(context: Context) {
        if (initialized) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // Note: In a real app, you would load actual sound files here
        // For now, we'll use ToneGenerator as fallback or add sound files later
        initialized = true
    }

    fun play(effect: SoundEffect, volume: Float = 1.0f) {
        if (!initialized) return

        // For now, we'll use vibration feedback instead of missing audio files
        // In production, load actual sound files:
        // soundMap[effect]?.let { soundId ->
        //     soundPool?.play(soundId, volume, volume, 1, 0, 1.0f)
        // }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        soundMap.clear()
        initialized = false
    }

    // Generate simple beep sounds programmatically as placeholder
    fun playBeep(frequency: Int = 440, duration: Int = 100) {
        // Placeholder for sound generation
        // In production, use actual audio files or ToneGenerator
    }
}
