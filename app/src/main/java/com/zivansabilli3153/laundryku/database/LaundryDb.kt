package com.zivansabilli3153.laundryku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zivansabilli3153.laundryku.model.Pesanan

@Database(
    entities = [Pesanan::class],
    version = 2,
    exportSchema = false
)
abstract class LaundryDb : RoomDatabase() {

    abstract val dao: PesananDao

    companion object {
        @Volatile
        private var INSTANCE: LaundryDb? = null

        fun getInstance(context: Context): LaundryDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LaundryDb::class.java,
                        "laundryku.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}