package si.uni_lj.fri.musicplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.media.MediaPlayer
import java.util.Locale
import java.io.IOException

class MusicService : Service() {

    var player: MediaPlayer? = null;


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
//                    Log.w(MainActivity.TAG, "Could not open file", e)
                    return
                }
                it.isLooping = true
                it.start()

                // display song info
//                musicInfoTextView?.text = this
//                Log.i(MainActivity.TAG, "Playing song $this")
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

    private fun getFiles(): List<String> =
        assets.list("")?.filter { it.lowercase(Locale.getDefault()).endsWith("mp3") }
            ?: emptyList()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer();
        play()
        return START_STICKY // service will restart if stopped
    }

    override fun onDestroy() {
        stop()
        player?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        private val TAG = MusicService::class.java.simpleName
    }
}