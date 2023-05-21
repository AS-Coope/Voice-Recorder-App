package com.example.voicerecorderapp.audiodatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudioDao {

    // insert an audio recording into the database
    @Insert
    suspend fun insertAudio(audio: Audio)

    // removes all audio recordings from the database
    @Delete
    suspend fun deleteAllAudio(audio: Audio)

    // remove a specific member of the database
    @Query("DELETE FROM audio_recordings WHERE audio_id = :aId")
    suspend fun deleteAudio(aId: Int)
}