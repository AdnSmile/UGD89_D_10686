package com.example.ugd89_d_10686_project1

import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.annotation.SuppressLint
import android.hardware.Camera
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    lateinit var sensorStatusTV : TextView
    lateinit var proximitySensor : Sensor
    lateinit var sensorManager : SensorManager

    private var mCamera : Camera? = null
    private var frontCamera : Camera? = null
    private var mCameraView : CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            mCamera = Camera.open()
        }catch (e:Exception) {
            Log.d("Error", "Failed to get Camera" + e.message)
        }

        if (mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? -> System.exit(0) }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            // on below line we are registering
            // our sensor with sensor manager
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

    }


    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // method to check accuracy changed in sensor.
        }

        override fun onSensorChanged(event: SensorEvent?) {
            // check if the sensor type is proximity sensor.
            if (event != null) {
                if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                    if (event.values[0] == 0f) {
                        // kalau near
                        mCamera?.stopPreview()
                        mCamera?.release()
                        try {
                            frontCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                        }catch (e:Exception) {
                            Log.d("Error", "Failed to get Camera" + e.message)
                        }

                        if (frontCamera != null) {
                            mCameraView = CameraView(this@MainActivity, frontCamera!!)
                            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                            camera_view.addView(mCameraView)
                        }
                        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
                            findViewById<View>(R.id.imgClose) as ImageButton
                        imageClose.setOnClickListener { view: View? -> System.exit(0) }
                    } else {
//                        finish()
                    }
                }
            }

        }
    }


}