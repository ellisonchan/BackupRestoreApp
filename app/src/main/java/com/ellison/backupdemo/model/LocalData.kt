package com.ellison.backupdemo.model

import android.app.backup.BackupManager
import android.app.backup.RestoreObserver
import android.content.Context
import android.util.Log
import com.ellison.backupdemo.R
import com.ellison.backupdemo.utils.Utils
import com.ellison.backupdemo.bean.CacheData
import com.ellison.backupdemo.bean.fakeMovie
import com.ellison.backupdemo.model.room.MovieDAO
import com.ellison.backupdemo.utils.Constants
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class LocalData @Inject constructor(private val movieDAO: MovieDAO,
                                    @ActivityContext private val context: Context,
                                    val backupManager: BackupManager){
    fun initData(): Boolean {
        return saveData() && saveFile() && saveDB()
    }

    private fun saveData(): Boolean {
        return Utils.saveImageToDisk(R.drawable.ic_image_preset, context)
    }

    private fun saveFile(): Boolean {
        return Utils.saveInstanceToDisk(fakeMovie, context)
    }

    private fun saveDB(): Boolean {
        return movieDAO.insert(fakeMovie) != -1L
    }

    fun loadData(): CacheData {
        val bitmap = Utils.readImageToDisk(context)
        val movieFromFile = Utils.readInstanceFromDisk(context)
        val movieFromDb = movieDAO.getMovie(1)
        return CacheData(bitmap, movieFromFile, movieFromDb)
    }

    fun backupData() {
        Log.d(Constants.TAG, "LocalData#backupData()")
        backupManager.dataChanged()
    }

    fun restoreData() {
        Log.d(Constants.TAG, "LocalData#restoreData()")

        backupManager.requestRestore(object: RestoreObserver() {
            override fun restoreStarting(numPackages: Int) {
                Log.d(Constants.TAG_BACKUP, "restoreStarting numPackages:$numPackages")
                super.restoreStarting(numPackages)
            }

            override fun onUpdate(nowBeingRestored: Int, currentPackage: String?) {
                Log.d(Constants.TAG_BACKUP, "restoreStarting nowBeingRestored:$nowBeingRestored currentPackage:$currentPackage")
                super.onUpdate(nowBeingRestored, currentPackage)
            }

            override fun restoreFinished(error: Int) {
                Log.d(Constants.TAG_BACKUP, "restoreFinished error:$error")
                super.restoreFinished(error)
            }
        })
    }
}