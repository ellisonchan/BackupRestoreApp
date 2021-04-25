package com.ellison.backupdemo

import android.app.backup.*
import android.os.Build
import android.os.ParcelFileDescriptor
import android.system.ErrnoException
import android.system.Os
import android.text.TextUtils
import android.util.Log
import com.ellison.backupdemo.utils.Constants
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MyBackupAgent : BackupAgentHelper() {
    private var needSkipRestore = false

    override fun onCreate() {
        Log.d(Constants.TAG_BACKUP, "onCreate()")
        super.onCreate()

        // Init helper for data, file, db and sp files.
        // FileBackupHelper(this, Constants.DATA_NAME).also { addHelper(Constants.BACKUP_KEY_DATA, it) }
        // FileBackupHelper(this, Constants.FILE_NAME).also { addHelper(Constants.BACKUP_KEY_FILE, it) }
        FileBackupHelper(this, Constants.DB_NAME).also { addHelper(Constants.BACKUP_KEY_DB, it) }
        SharedPreferencesBackupHelper(this, Constants.SP_NAME).also { addHelper(Constants.BACKUP_KEY_SP, it) }
    }

    // Call back when data size over limit.
    override fun onQuotaExceeded(backupDataBytes: Long, quotaBytes: Long) {
        Log.d(Constants.TAG_BACKUP, "onQuotaExceeded() size:$backupDataBytes limit:$quotaBytes")
        super.onQuotaExceeded(backupDataBytes, quotaBytes)
        // Todo
    }

    override fun onDestroy() {
        Log.d(Constants.TAG_BACKUP, "onDestroy()")
        super.onDestroy()
        // Ensure temp source file is removed after backup or restore finished.
        ensureBackupSourceFileRemoved()
    }

    // Key-value backup mode.
    override fun onBackup(
            oldState: ParcelFileDescriptor?,
            data: BackupDataOutput?,
            newState: ParcelFileDescriptor?
    ) {
        Log.d(Constants.TAG_BACKUP, "onBackup()")
        super.onBackup(oldState, data, newState)
    }

    override fun onRestore(
            data: BackupDataInput?,
            appVersionCode: Int,
            newState: ParcelFileDescriptor?
    ) {
        Log.d(Constants.TAG_BACKUP, "onRestore() appVersionCode:$appVersionCode")
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        if (packageInfo.versionCode != appVersionCode) {
            // Do something.
        } else {
            super.onRestore(data, appVersionCode, newState)
        }
    }

    // Auto backup mode.
    override fun onFullBackup(data: FullBackupDataOutput?) {
        Log.d(Constants.TAG_BACKUP, "onFullBackup()")
        // Make backup source file before full backup invoke.
        writeBackupSourceToFile()
        super.onFullBackup(data)

        if (data != null) {
            if ((data.transportFlags and FLAG_CLIENT_SIDE_ENCRYPTION_ENABLED) != 0) {
                Log.d(Constants.TAG_BACKUP, "onFullBackup() CLIENT ENCRYPTION NEED")
            }
        }
    }

//    @Throws(IOException::class)
//    fun onRestoreFile(data: ParcelFileDescriptor?, size: Long,
//                               type: Int, domain: String, path: String, mode: Long, mtime: Long) {
//        Log.d(Constants.TAG_BACKUP, "onRestoreFile() type:$type domain:$domain path:$path mode:$mode mtime:$mtime")
//    }

    override fun onRestoreFile(
            data: ParcelFileDescriptor?,
            size: Long,
            destination: File?,
            type: Int,
            mode: Long,
            mtime: Long
    ) {
        Log.d(Constants.TAG_BACKUP, "onRestoreFile() destination:$destination"
                + " type:$type "
                + " mode:$mode "
                + " mtime:$mtime"
                + " currentDevice:${Build.MODEL}"
                + " needSkipRestore:$needSkipRestore")
        if (!needSkipRestore) {
            val sourceDevice = readBackupSourceFromFile(destination)
            Log.d(Constants.TAG_BACKUP, "onRestoreFile() sourceDevice:$sourceDevice")

            // Mark need skip restore if source got and not match current device.
            if (!TextUtils.isEmpty(sourceDevice) && !sourceDevice.equals(Build.MODEL)) {
                needSkipRestore = true
                // Toast.makeText(this, R.string.restore_skip, Toast.LENGTH_LONG).show()
            }
        }

        if (!needSkipRestore) {
            Log.d(Constants.TAG_BACKUP, "onRestoreFile() invoke restore")
            // Invoke restore if skip flag set.
            super.onRestoreFile(data, size, destination, type, mode, mtime)
        } else {
            Log.d(Constants.TAG_BACKUP, "onRestoreFile() skip restore and consume")
            // Consume data to keep restore stream go.
            consumeData(data!!, size, type, mode, mtime, null)
            // super.onRestoreFile(data, size, null, type, mode, mtime)
        }
    }

    // Callback when restore finished.
    override fun onRestoreFinished() {
        Log.d(Constants.TAG_BACKUP, "onRestoreFinished()")
        super.onRestoreFinished()
    }

    private fun writeBackupSourceToFile() {
        val sourceFile = File(dataDir.absolutePath + File.separator
                + Constants.BACKUP_SOURCE_FILE_PREFIX + Build.MODEL)
        if (!sourceFile.exists()) {
            val result = sourceFile.createNewFile()
            Log.d(Constants.TAG_BACKUP, "writeBackupSourceToFile() sourceFile:$sourceFile create:$result")
        }
    }

    private fun readBackupSourceFromFile(file: File?): String {
        Log.d(Constants.TAG_BACKUP, "readBackupSourceFromFile() file:$file")
        if (file == null) return ""
        var decodeDeviceSource = ""

        // Got data file with backup source mark.
        if (file.name.startsWith(Constants.BACKUP_SOURCE_FILE_PREFIX)) {
            decodeDeviceSource = file.name.replace(Constants.BACKUP_SOURCE_FILE_PREFIX, "")
        }

        Log.d(Constants.TAG_BACKUP, "readBackupSourceFromFile() source:$decodeDeviceSource")
        return decodeDeviceSource
    }

    private fun ensureBackupSourceFileRemoved() {
        val sourceFile = File(dataDir.absolutePath + File.separator
                + Constants.BACKUP_SOURCE_FILE_PREFIX + Build.MODEL)
        if (sourceFile.exists()) {
            val result = sourceFile.delete()
            Log.d(Constants.TAG_BACKUP, "ensureBackupSourceFileRemoved() sourceFile:$sourceFile"
                    + " delete:$result")
        }
    }

    @Throws(IOException::class)
    fun consumeData(data: ParcelFileDescriptor,
                    size: Long, type: Int, mode: Long, mtime: Long, outFile: File?) {
        var size = size
        var mode = mode
        var out: FileOutputStream? = null

            // Pull the data from the pipe, copying it to the output file, until we're done
            try {
                if (outFile != null) {
                    val parent = outFile.parentFile
                    if (!parent.exists()) {
                        parent.mkdirs()
                    }
                    out = FileOutputStream(outFile)
                }
            } catch (e: IOException) {
            }
            val buffer = ByteArray(32 * 1024)
            val origSize = size
            val `in` = FileInputStream(data.fileDescriptor)
            while (size > 0) {
                val toRead = if (size > buffer.size) buffer.size else size.toInt()
                val got = `in`.read(buffer, 0, toRead)
                if (got <= 0) {
                    break
                }
                if (out != null) {
                    try {
                        out.write(buffer, 0, got)
                    } catch (e: IOException) {
                        out.close()
                        out = null
                        outFile?.delete()
                    }
                }
                size -= got.toLong()
            }
            out?.close()

        // Now twiddle the state to match the backup, assuming all went well
        if (mode >= 0 && outFile != null) {
            try {
                // explicitly prevent emplacement of files accessible by outside apps
                mode = mode and 448
                Os.chmod(outFile.path, mode.toInt())
            } catch (e: ErrnoException) {
            }
            outFile.setLastModified(mtime)
        }
    }
}