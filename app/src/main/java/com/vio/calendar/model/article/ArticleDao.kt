package com.vio.calendar.model.article

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface ArticleDao {
    @get:Query("SELECT * FROM article_table")
    val all: <List<Article>>

    @Insert(onConflict = REPLACE)
    fun insert(article: Article)

    @Query("DELETE FROM article_table WHERE id = :id")
    fun delete(id: String?)

    @Query("DELETE FROM article_table")
    fun deleteAll()

    @Update
    fun update(article: Article)
}