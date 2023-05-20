package com.example.voicerecorderapp.recording

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream

class Recorder (private val context:Context){
   private var recorder: MediaRecorder? = null

    @RequiresApi(Build.VERSION_CODES.S)
    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)
        } else MediaRecorder()
    }
    @RequiresApi(Build.VERSION_CODES.S)
    fun start(outputFile: File) {
        createRecorder().apply{
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // apparently MPEG_4 = MP3
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare() // preparing to record
            start() // starts the recording

            recorder = this
        }
    }

    fun stop() {
        recorder?.stop()
        recorder?.reset() // allows recording again
        recorder = null // whatever is being recorded next replaces null
    }
}