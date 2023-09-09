package com.example.voicerecorderapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
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
        // moved audioDao from here
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
        // NOTE: notifyDataSetChanged() does not indicate which item in the data set has changed
        // so all observers assume all the data has been changed (use it when all the data will be affected)
        // if deleting use notifyItemRemoved(), or for inserting use notifyItemInserted()

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
            Toast.makeText(this, "$audioFileName is playing", Toast.LENGTH_SHORT).show()
            // TODO(Detect when the audio itself has ended and stop playing even if the user didn't
            //      click the button to stop audio playing)

        } else {
            // ui related updates
            //recordingPlayBtn.setImageResource(R.drawable.play_audio)

            // logic update
            isPlaying = false
            player.stop()
            Toast.makeText(this, "$audioFileName stopped playing", Toast.LENGTH_SHORT).show()
        }
    }

    // when view models are added, will eventually change how audio is deleted
    // and reflected on the UI

    // eventually, the functionality of the Long Click will be to select an audio file
    // and not to automatically delete it

    // additionally there should be a dialog box that asks if the user wants to delete the audio recording
    // add that in the v1.1

    // Need this here to have the overall class work but remove when using the actual longClickListener
    override fun onItemLongClickListener(position: Int) {
        TODO("Not yet implemented")
    }
    // Disabled OnItemLongClickListener (currently deletes audio recording); Uncomment to disable
    /*
    override fun onItemLongClickListener(position: Int) {
        // finding the audio in the recycler view
        var audio = audioFiles[position]
        var audioFileName = "${audio.aName}.mp3" // technically not necessary as I could just call
                                                 // audio.aName wherever I need to reference the name
                                                 // but it does help

        // didn't want to put here but can't be referenced from inside the onCreate()
        // where the original one is
        val audioDao2 = AudioDatabase.getDatabase(application).audioEntityDao()

        Toast.makeText(this, "$audioFileName has been deleted!", Toast.LENGTH_LONG).show()

        // deleting the audio from the recycler view
        CoroutineScope(Dispatchers.IO).launch {
            // io operations, like database operations are resource intensive so do it on
            // a different thread than the UI thread
            audioDao2.deleteAudio(audio.aId)
        }
        // removes the item from the list shown in the recycler view
        audioFiles.removeAt(position)
        // notifies the view of these changes made to the list of audios
        audioAdapter.notifyItemRemoved(position)
    }
    */
}