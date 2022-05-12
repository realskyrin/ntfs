package cn.skyrin.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

class RingerModeChangeHelper(var context: Context) {
    private var ringerModeChangeListener: RingerModeChangeListener? = null
    private var ringerBroadCastReceiver: RingerBroadCastReceiver? = null
    private var registered = false

    companion object {
        const val RINGER_MODE_CHANGED_ACTION = AudioManager.RINGER_MODE_CHANGED_ACTION
    }

    private var audioManager: AudioManager? =
        context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager


    fun registerModeChangeListener(ringerModeChangeListener: RingerModeChangeListener) {
        if (registered) return // avoid duplicate register

        this.ringerModeChangeListener = ringerModeChangeListener
        ringerBroadCastReceiver = RingerBroadCastReceiver()
        val filter = IntentFilter()
        filter.addAction(RINGER_MODE_CHANGED_ACTION)
        if (ringerBroadCastReceiver != null) {
            context.registerReceiver(ringerBroadCastReceiver!!, filter)
            registered = true
        }
    }

    fun unregisterReceiver() {
        if (ringerBroadCastReceiver != null) {
            try {
                context.unregisterReceiver(ringerBroadCastReceiver!!)
            } catch (e: Exception) {
            }
            ringerBroadCastReceiver = null
            registered = false
        }
    }

    interface RingerModeChangeListener {
        fun onChange(mode: Int)
    }

    inner class RingerBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == RINGER_MODE_CHANGED_ACTION) {
                val currentMode = audioManager?.ringerMode
                ringerModeChangeListener?.onChange(currentMode ?: AudioManager.RINGER_MODE_NORMAL)
            }
        }
    }
}