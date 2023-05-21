package com.example.voicerecorderapp

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.voicerecorderapp.playing.Player
import com.example.voicerecorderapp.recording.Recorder
import java.io.File

class MainActivity : AppCompatActivity() {
    private val recorder by lazy {
        Recorder(applicationContext)
    }

    private val player by lazy {
        Player(applicationContext)
    }

    private var audioFile: File? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // requesting permission to record the user's audio
        // given recording audio has the potential to compromise the user's
        // privacy (this is considered a dangerous permission), they have
        // to be asked if they want to record audio. It will not work otherwise
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

        // View elements declarations and assignments
        val spRecordButton = findViewById<Button>(R.id.spBtnRecordAudio)
        val spStopRecordButton = findViewById<Button>(R.id.spBtnStopRecordAudio)
        val spPlayButton = findViewById<Button>(R.id.spBtnPlayAudio)
        val spStopPlayButton = findViewById<Button>(R.id.spBtnStopPlayAudio)

        // Operations for each button
        spRecordButton.setOnClickListener {
            File(cacheDir, "audio.mp3").also{
                recorder.start(it)
                audioFile = it
                Toast.makeText(applicationContext, "Recording Has Begun", Toast.LENGTH_SHORT).show()
}
        }

        spStopRecordButton.setOnClickListener {
            recorder.stop()
            Toast.makeText(applicationContext, "Recording Has Stopped", Toast.LENGTH_SHORT).show()
        }

        spPlayButton.setOnClickListener {
            player.playFile(audioFile?: return@setOnClickListener)
            Toast.makeText(applicationContext, "Playing Has Begun", Toast.LENGTH_SHORT).show()
        }

        spStopPlayButton.setOnClickListener {
            player.stop()
            Toast.makeText(applicationContext, "Playing Has Stopped", Toast.LENGTH_SHORT).show()
        }
    }
}