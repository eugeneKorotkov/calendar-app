package com.vio.calendar.model.article

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Article::class], version = 1)

abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        private val lock = Any()
        private const val DB_NAME = "movie_database"
        private var INSTANCE: ArticleDatabase? = null

        fun getInstance(application: Application): ArticleDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE =
                        Room.databaseBuilder(application, ArticleDatabase::class.java, DB_NAME)
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}