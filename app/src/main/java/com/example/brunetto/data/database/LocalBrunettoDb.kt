package com.example.brunetto.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.brunetto.data.lastInput.LastInput
import com.example.brunetto.data.lastInput.LastInputDao

@Database(
    version = 1,
    entities = [
        LastInput::class,
    ],
    //autoMigrations = [AutoMigration ( from = 1, to = 2,)],
    exportSchema = false)
abstract class LocalBrunettoDb: RoomDatabase() {

    abstract fun lastInputDao(): LastInputDao

    companion object{
        @Volatile
        private var INSTANCE: LocalBrunettoDb? = null

        /*val migration_1_2: Migration = object: Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_table ADD COLUMN tryNewColumn TEXT DEFAULT '159'")
            }
        }*/

        fun getDatabase(context: Context): LocalBrunettoDb {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalBrunettoDb::class.java,
                    "user_database"
                )
                    // .addMigrations(migration_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}