package com.example.voicerecorderapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.voicerecorderapp.audiodatabase.Audio
import com.example.voicerecorderapp.audiodatabase.AudioDatabase
import com.example.voicerecorderapp.playing.Player
import com.example.voicerecorderapp.recording.Recorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    // logs should not be on the
    var MainActivityTag: String = "MainActivity"
    // creating the recorder variable but not initializing it until later in the activity
    private val recorder by lazy {
        Recorder(applicationContext)
    }

    private val player by lazy {
        Player(applicationContext)
    }

    var isRecording: Boolean = false
    //private var audioFile: File? = null

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

        // creating an audioDao object for interacting with the database
        val audioDao = AudioDatabase.getDatabase(application).audioEntityDao()

        // View elements declarations and assignments
        //val spPlayButton = findViewById<Button>(R.id.spBtnPlayAudio)
        //val spStopPlayButton = findViewById<Button>(R.id.spBtnStopPlayAudio)

        // image button views
        val recordButtonIb = findViewById<ImageView>(R.id.recordIb)
        val audioListButtonIb = findViewById<ImageView>(R.id.audioListIb)

        // setting up file details
        var audioDateTime = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss").format(Date())
        var audioRelPath = ""
        var audioFile: File? = null

        recordButtonIb.setOnClickListener {
            // recording started
            if (isRecording == false) {
                isRecording = true
                // UI changes - changing button image
                recordButtonIb.setImageResource(R.drawable.recording_in_progress)
                recordButtonIb.setBackgroundResource(R.drawable.recording_in_progress_background)

                // logic for recording file //

                // 'catching' or saving the current date time (to the second)
                // as soon as recording has started
                val currentDateTime = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss").format(Date())
                audioDateTime = currentDateTime
                audioRelPath = "audio_vra_000_${audioDateTime}"

                // creating the audio file
                File(cacheDir, "${audioRelPath}.mp3").also {

                    recorder.start(it) // recording the audio from mic and saving it in the file
                    audioFile = it // saving the (full) file path to this variable

                    // Visual indication to the user that audio is being recorded
                    Toast.makeText(applicationContext, "Recording Has Begun", Toast.LENGTH_SHORT)
                        .show()
                    Toast.makeText(applicationContext, audioRelPath, Toast.LENGTH_LONG).show()
                }
            } else {
                // recording stopped
                isRecording = false
                // UI changes - changing button image
                recordButtonIb.setImageResource(R.drawable.no_recording)
                recordButtonIb.setBackgroundResource(R.drawable.no_recording_background)

                recorder.stop()
                Toast.makeText(applicationContext, "Recording Has Stopped", Toast.LENGTH_SHORT)
                    .show()
                Toast.makeText(applicationContext, "File Name is: $audioFile", Toast.LENGTH_LONG)
                    .show()
                val arbitraryDuration = "00:10.00"

                // Saving the file to a database //

                // if audioFile isn't null, then save it to nonNullable, otherwise, save this path
                val nonNullable: File = audioFile ?: File("default/path/to/file")

                // converting file size to kilobytes then saving that
                val audioFileSize = "${nonNullable.length() / 1024}KB"

                // finally, creating the audio record
                val audioRec =
                    Audio(0, audioRelPath, arbitraryDuration, audioDateTime, audioFileSize)

                // database operations are resource intensive; do them on a separate thread
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // storing the audio metadata in the room database
                        audioDao.insertAudio(audioRec)
                        Log.i(MainActivityTag, "Audio stored in Database")
                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            "$audioRelPath has not been saved",
                            Toast.LENGTH_LONG
                        ).show()
                        e.printStackTrace()
                        Log.e(MainActivityTag, "ERROR 70737669: Audio file not saved to database")
                    }
                } // end of CoroutineScope
            } // end of else (isRecording check)
        } // end of setOnClickListener

        // going to the audio recordings list
        audioListButtonIb.setOnClickListener {
            Intent(this, AudioList::class.java).also{
                startActivity(it)
            }
        }

        /*
        spPlayButton.setOnClickListener {
            player.playFile(audioFile?: return@setOnClickListener)
            Toast.makeText(applicationContext, "Playing Has Begun", Toast.LENGTH_SHORT).show()
        }

        spStopPlayButton.setOnClickListener {
            player.stop()
            Toast.makeText(applicationContext, "Playing Has Stopped", Toast.LENGTH_SHORT).show()
        }*/

    } // end of onCreate method
}