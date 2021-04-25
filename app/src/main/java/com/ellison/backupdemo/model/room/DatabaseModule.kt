package com.ellison.backupdemo.model.room

import android.app.Application
import androidx.room.Room
import com.ellison.backupdemo.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideMovieDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(application, MovieDatabase::class.java, Constants.DB_NAME)
            .fallbackToDestructiveMigrationFrom(1)
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDAO(movieDatabase: MovieDatabase): MovieDAO {
        return movieDatabase.movieDao()
    }
}