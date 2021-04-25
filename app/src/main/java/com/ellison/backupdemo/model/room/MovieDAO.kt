package com.ellison.backupdemo.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ellison.backupdemo.bean.Movie

@Dao
interface MovieDAO {
    @Insert
    fun insert(movie: Movie?): Long?

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): Movie?
}