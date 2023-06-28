package com.example.voicerecorderapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voicerecorderapp.audiodatabase.Audio
import com.example.voicerecorderapp.audiodatabase.AudioDatabase
import com.example.voicerecorderapp.playing.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AudioList : AppCompatActivity(), onItemClickListener {

    private lateinit var audioFiles: ArrayList<Audio>
    private lateinit var audioAdapter: Adapter
    private val player by lazy {
        Player(applicationContext)
    }

    var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_list)

        // view related initializations
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)


        // database related initializations
        val audioDao = AudioDatabase.getDatabase(application).audioEntityDao()
        audioFiles = ArrayList()
        audioAdapter = Adapter(audioFiles, this)

        recyclerview.apply{
            adapter = audioAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // getting the data from the database and storing it in the arraylist to display
        CoroutineScope(Dispatchers.IO).launch {
            audioFiles.addAll(audioDao.getAllRecords()) // database operations happen on a background thread
        }
        audioAdapter.notifyDataSetChanged() // but view/ui updates have to happen on the ui thread

    }

    override fun onItemClickListener(position: Int) {

        var recordingPlayBtn = findViewById<ImageButton>(R.id.recordingPlay)
        // determining which audio to play in the recycler view
        var audio = audioFiles[position]
        var audioFileName = "${audio.aName}.mp3"

        // getting the audio file from the app's cache
        var theAudioFile = File(cacheDir, audioFileName)

        if (isPlaying == false){
        //println(theAudioFile.absoluteFile)

            // ui related updates
            //recordingPlayBtn.setImageResource(R.drawable.stop_audio)

            // logic update
            isPlaying = true
            player.playFile(theAudioFile)
        } else {
            // ui related updates
            //recordingPlayBtn.setImageResource(R.drawable.play_audio)

            // logic update
            isPlaying = false
            player.stop()
        }
    }
}