package com.example.voicerecorderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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