package com.example.voicerecorderapp.audiodatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//import java.sql.Date

@Entity(tableName="audio_recordings")
data class Audio(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "audio_id") val aId: Int,
    @ColumnInfo(name = "audio_name") val aName: String,
    @ColumnInfo(name = "audio_length") val aLength: String,
    @ColumnInfo(name = "audio_data_time") val audioDataTime: String,
    @ColumnInfo(name = "audio_file_size") val audioSize: String
)
