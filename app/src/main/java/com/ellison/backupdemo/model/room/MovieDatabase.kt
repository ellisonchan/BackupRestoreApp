package com.ellison.backupdemo.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ellison.backupdemo.bean.Movie

@Database(version = 1, entities=[Movie::class])
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDAO
}