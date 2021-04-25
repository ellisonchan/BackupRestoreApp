package com.ellison.backupdemo.utils

class Constants {
    companion object {
        const val TAG = "BackupRestoreApp"
        const val TAG_BACKUP = "BackupRestoreAgent"

        const val DATA_NAME = "Post.jpg"
        const val BACKUP_KEY_DATA = "datas"

        const val FILE_NAME = "MovieFile"
        const val BACKUP_KEY_FILE = "files"

        const val DB_NAME = "Movie.db"
        const val BACKUP_KEY_DB = "dbs"

        const val SP_NAME = "SimpleData"
        const val SP_INIT_KEY = "alreadyInit"
        const val BACKUP_KEY_SP = "prefs"

        const val BACKUP_SOURCE_FILE_PREFIX = "backup-source-"
    }
}