package si.uni_lj.fri.musicplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.media.MediaPlayer
import android.os.Binder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.Locale
import java.io.IOException

class MusicService : Service() {

    private var player: MediaPlayer? = null
    var song = ""
    internal class LocalBinder(val service: MusicService): Binder()
    private val binder = LocalBinder(this)

    /**
     * Starts the music player playback
     */
    fun play() {
        player?.let {
            if (it.isPlaying) {
                return
            }
            getFiles().random().apply {
                try {
                    val descriptor = assets.openFd(this)
                    it.setDataSource(
                        descriptor.fileDescriptor,
                        descriptor.startOffset,
                        descriptor.length
                    )
                    descriptor.close()
                    it.prepare()
                } catch (e: IOException) {
                    Log.w(MusicService.TAG, "Could not open file", e)
                    song = "Error :("
                    broadcastSong()
                    return
                }
                it.isLooping = true
                it.start()

                // display song info
//                musicInfoTextView?.text = this
                song = this
                broadcastSong()
                Log.i(MusicService.TAG, "Playing song $this")
            }
        }
    }

    /**
     * Stops the music player playback
     */
    fun stop() {
        player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.reset()
        }

        // display song info
//        musicInfoTextView?.text = ""
    }
    fun getSongName(): String {
        return song
    }

    fun broadcastPlease() {
        broadcastSong()
    }
    private fun broadcastSong() {
        val intent = Intent("mplayer.song")
        intent.putExtra("song", song)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun getFiles(): List<String> =
        assets.list("")?.filter { it.lowercase(Locale.getDefault()).endsWith("mp3") }
            ?: emptyList()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer()
//        play()
        return START_STICKY // service will restart if stopped
    }

    override fun onDestroy() {
        stop()
        player?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder  {
        return binder
    }

    override fun onRebind(intent: Intent?) {
        Log.i(TAG, "Rebind!")
        super.onRebind(intent)
    }

    companion object {
        private val TAG = MusicService::class.java.simpleName
    }
}