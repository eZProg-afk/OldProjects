package spiral.bit.dev.lgbtswipe.other

import android.media.MediaPlayer
import java.io.File

class AppVoicePlayer {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var file: File

    fun getPlayerSessionID(): Int {
        initPlayer()
        return mediaPlayer.audioSessionId
    }

    fun playVoice(messageKey: String, fileUrl: String, function: () -> Unit) {
        file = File(ACTIVITY.filesDir, messageKey)
        if (file.exists() && file.length() > 0 && file.isFile) {
            startPlayer {
                function()
            }
        } else {
            file.createNewFile()
            getFileFromStorage(file, fileUrl) {
                startPlayer {
                    function()
                }
            }
        }
    }


    private fun startPlayer(function: () -> Unit) {
        try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                stopPlayer {
                    function()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopPlayer(function: () -> Unit) {
        try {
            mediaPlayer.stop()
            mediaPlayer.reset()
            function()
        } catch (e: Exception) {
            e.printStackTrace()
            function()
        }
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }

    fun initPlayer() {
        mediaPlayer = MediaPlayer()
    }
}