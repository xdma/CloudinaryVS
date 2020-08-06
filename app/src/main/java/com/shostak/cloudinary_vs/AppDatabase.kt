package com.shostak.cloudinary_vs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shostak.cloudinary_vs.model.SubTitle
import com.shostak.cloudinary_vs.model.SubTitleDao

@Database(entities = [SubTitle::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subTitleDao(): SubTitleDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "cloudinary_vs"
                ).build()
            }

            return INSTANCE as AppDatabase
        }
    }


}
