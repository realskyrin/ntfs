package cn.skyrin.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import timber.log.Timber
import java.lang.Exception

class VolumeChangeHelper(var context: Context) {

    private var mVolumeBroadCastReceiver: VolumeBroadCastReceiver? = null
    private var mVolumeChangeListener: VolumeChangeListener? = null
    private var registered = false

    companion object {
        const val VOLUME_CHANGE_ACTION = "android.media.VOLUME_CHANGED_ACTION"
        const val EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE"
    }

    private var audioManager: AudioManager? =
        context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    fun registerVolumeChangeListener(volumeChangeListener: VolumeChangeListener) {
        if (registered) return // avoid duplicate register

        mVolumeChangeListener = volumeChangeListener
        mVolumeBroadCastReceiver = VolumeBroadCastReceiver()
        val filter = IntentFilter()
        filter.addAction(VOLUME_CHANGE_ACTION)
        if (mVolumeBroadCastReceiver != null) {
            //注册这个广播
            context.registerReceiver(mVolumeBroadCastReceiver!!, filter)
            registered = true
        }
    }

    fun unregisterReceiver() {
        if (mVolumeBroadCastReceiver != null) {
            try {
                context.unregisterReceiver(mVolumeBroadCastReceiver!!)
            } catch (e: Exception) {
            }
            mVolumeBroadCastReceiver = null
            registered = false
        }
    }

    interface VolumeChangeListener {
        fun onVolumeDownToMin()
        fun onVolumeUp()
        fun onVolumeChange()
    }

    //定义一个想监听音量变化的广播接受者
    inner class VolumeBroadCastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent?.action == VOLUME_CHANGE_ACTION){
                mVolumeChangeListener?.onVolumeChange()
            }

            if (intent?.action == VOLUME_CHANGE_ACTION && intent.getIntExtra(
                    EXTRA_VOLUME_STREAM_TYPE,
                    -1
                ) == AudioManager.STREAM_MUSIC
            ) {
                val currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: -1
                if (currentVolume > 0) {
                    mVolumeChangeListener?.onVolumeUp()
                } else if (currentVolume == 0) {
                    mVolumeChangeListener?.onVolumeDownToMin()
                }
            }
        }
    }
}