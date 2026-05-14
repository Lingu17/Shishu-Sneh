package com.example.shishusneh.ui.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceFeedbackManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.setLanguage(Locale.US)
            isReady = true
        }
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        val prefs = context.getSharedPreferences("shishu_sneh", Context.MODE_PRIVATE)
        if (isReady && prefs.getBoolean("voice_enabled", true)) {
            tts?.speak(text, queueMode, null, null)
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
