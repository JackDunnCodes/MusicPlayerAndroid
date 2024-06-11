package si.uni_lj.fri.musicplayer


import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private var musicInfoTextView: TextView? = null
    private var startServiceButton: Button? = null
    private var stopServiceButton: Button? = null
    private var aboutButton: Button? = null
    private var playButton: Button? = null
    private var stopButton: Button? = null

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


        stopButton?.setOnClickListener {

        }
        playButton?.setOnClickListener {

        }

        startServiceButton?.setOnClickListener {
            val musicIntent = Intent(this@MainActivity, MusicService::class.java)
            startService(musicIntent)

        }
        stopServiceButton?.setOnClickListener {
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
    }

    override fun onPause() {
        super.onPause()
    }


    /**
     * Returns the list of mp3 files in the assets folder
     *
     * @return
     */
    private fun getFiles(): List<String> =
        assets.list("")?.filter { it.lowercase(Locale.getDefault()).endsWith("mp3") }
            ?: emptyList()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}