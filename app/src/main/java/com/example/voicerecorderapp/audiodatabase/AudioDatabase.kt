package com.example.voicerecorderapp.audiodatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Audio::class], version = 1, exportSchema = false)
abstract class AudioDatabase: RoomDatabase() {

    // creating a function which will be used to reference the functions
    // in the audio dao interface
    abstract fun audioEntityDao(): AudioDao

    companion object {
        @Volatile // writes to this instance of the database are seen by all threads
        private var INSTANCE: AudioDatabase? = null

        // creating the audio database once another instance of it does not exist

        fun getDatabase(context: Context): AudioDatabase {
            val temporaryInstance = INSTANCE
            // if the instance of the database is not null (the database exists) then
            // return that database to whatever is calling getDatabase
            if (temporaryInstance!= null){
                return temporaryInstance
            }
            // synchronized ensures that this thread that is creating the database is the only
            // thread that has access to this object (the database) while the it is created,
            // essentially, locking it from all other threads
            synchronized(this){
                val dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AudioDatabase::class.java, // the database here has to extend RoomDatabase()
                    "audio_database"
                ).build()
                // using the builder design pattern to choose what specifically is wanted
                // when building this database
                INSTANCE = dbInstance
                // once the database is created, store that as the instance of the database
                // this facilitates checks to see whether an instance of this database already exists
                return dbInstance
                // provide whatever called getDatabase() with this newly created instance of
                // AudioDatabase
            }
        }
    }

}