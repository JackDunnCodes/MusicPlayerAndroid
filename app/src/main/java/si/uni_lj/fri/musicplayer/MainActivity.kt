package si.uni_lj.fri.musicplayer


import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private var musicInfoTextView: TextView? = null
    private var startServiceButton: Button? = null
    private var stopServiceButton: Button? = null
    private var aboutButton: Button? = null
    private var playButton: Button? = null
    private var stopButton: Button? = null

    private var service: MusicService? = null
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "onServiceConnected()")
            this@MainActivity.service = (service as MusicService.LocalBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "onServiceDisconnected()")
            this@MainActivity.service = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate()")

        setContentView(R.layout.activity_main)

        musicInfoTextView = findViewById(R.id.musicInfoTextView)
        startServiceButton = findViewById(R.id.startServiceButton)
        stopServiceButton = findViewById(R.id.stopServiceButton)
        aboutButton = findViewById(R.id.aboutButton)
        stopButton = findViewById(R.id.stopButton)
        playButton = findViewById(R.id.playButton)



        playButton?.setOnClickListener {
            service?.play()
        }
        stopButton?.setOnClickListener {
            service?.stop()
        }

        startServiceButton?.setOnClickListener {
            val musicIntent = Intent(this@MainActivity, MusicService::class.java)
            startService(musicIntent)
            bindService(musicIntent, connection, BIND_AUTO_CREATE)
        }
        stopServiceButton?.setOnClickListener {
            unbindService(connection)
            service = null
            val musicIntent = Intent(this@MainActivity, MusicService::class.java)
            stopService(musicIntent)
        }
        aboutButton?.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AboutActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
        if (isServiceRunning()) {
            val musicIntent = Intent(this@MainActivity, MusicService::class.java)
            bindService(
                musicIntent, connection, BIND_AUTO_CREATE
            )
        }
    }
    @Suppress("DEPRECATION")
    private fun isServiceRunning(): Boolean =
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Int.MAX_VALUE)
            .any { it.service.className == MusicService::class.java.canonicalName }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "onStop()")
        service?.let {
            unbindService(connection)
            service= null
        }
        super.onStop()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}