package com.ellison.backupdemo.viewmodel

import android.app.backup.BackupManager
import android.app.backup.RestoreObserver
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ellison.backupdemo.utils.Constants
import com.ellison.backupdemo.model.LocalData
import com.ellison.backupdemo.bean.CacheData
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext

class TransferViewModel @ViewModelInject constructor(
        private val localData: LocalData
) : ViewModel() {
    private val cacheLiveData = MutableLiveData<CacheData>()

    fun initAndLoadData(sp: SharedPreferences) {
        // Save data, file and db.
        if (localData.initData()) {
            Log.d(Constants.TAG, "initAndLoadData() INIT SUCCEED")
            // Mark already init flag if save data succeed.
            sp.edit().putBoolean(Constants.SP_INIT_KEY, true).apply()
        }
    }

    fun loadData(lifecycleOwner: LifecycleOwner, observer: Observer<CacheData>) {
        cacheLiveData.observe(lifecycleOwner, observer)
        val cacheData = localData.loadData()
        cacheLiveData.postValue(cacheData)
    }

    fun backupData() {
        Log.d(Constants.TAG, "TransferViewModel#backupData()")
        localData.backupData()
    }

    fun restoreData() {
        Log.d(Constants.TAG, "TransferViewModel#restoreData()")
        localData.restoreData()
    }
}