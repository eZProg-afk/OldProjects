package spiral.bit.dev.lgbtswipe.other

import android.media.MediaRecorder
import java.io.File

class AppVoiceRecorder {

    private val mediaRecorder = MediaRecorder()
    private lateinit var file: File
    private lateinit var msgKey: String

    fun startRecord(messageKey: String) {
        try {
            msgKey = messageKey
            createFileForRecord()
            prepareMediaRecorder()
            mediaRecorder.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prepareMediaRecorder() {
        mediaRecorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(file.absolutePath)
            prepare()
        }
    }

    private fun createFileForRecord() {
        file = File(ACTIVITY.filesDir, msgKey)
        file.createNewFile()
    }

    fun stopRecord(onSuccess: (file: File, messageKey: String) -> Unit) {
        try {
            mediaRecorder.stop()
            onSuccess(file, msgKey)
        } catch (e: Exception) {
            e.printStackTrace()
            file.delete()
        }
    }

    fun releaseRecorder() {
        try {
            mediaRecorder.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}