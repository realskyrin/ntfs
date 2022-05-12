package cn.skyrin.common.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class ShakeSensor : SensorEventListener {

    private var listener: ShakeListener? = null
    private var mShakeTimestamp: Long = 0
    private var mShakeCount = 0
    /*
	 * The gForce that is necessary to register as shake.
	 * Must be greater than 1G (one earth gravity unit).
	 * You can install "G-Force", by Blake La Pierre
	 * from the Google Play Store and run it to see how
	 *  many G's it takes to register a shake
	 */
    private val SHAKE_THRESHOLD_GRAVITY = 2.7f
    private val SHAKE_SLOP_TIME_MS = 500
    private val SHAKE_COUNT_RESET_TIME_MS = 3000

    fun registerShakeListener(listener: ShakeListener) {
        this.listener = listener
    }

    interface ShakeListener {
        fun onShake(count: Int)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        // gForce will be close to 1 when there is no movement.

        // gForce will be close to 1 when there is no movement.
        val gForce: Float = kotlin.math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis()
            // ignore shake events too close to each other (500ms)
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return
            }

            // reset the shake count after 3 seconds of no shakes
            if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                mShakeCount = 0
            }
            mShakeTimestamp = now
            mShakeCount++
            listener?.onShake(mShakeCount)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // ignore
    }
}