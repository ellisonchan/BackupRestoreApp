package com.ellison.backupdemo.model.backup

import android.app.Activity
import android.app.Application
import android.app.backup.BackupManager
import android.content.Context
import androidx.room.Room
import com.ellison.backupdemo.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class BackupModule {
    @Singleton
    @Provides
    fun provideMovieDatabase(application: Application): BackupManager {
        // return application.getSystemService(BackupManager::class.java)
        return BackupManager(application)
    }
}