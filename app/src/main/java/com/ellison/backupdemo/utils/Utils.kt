package com.ellison.backupdemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ellison.backupdemo.bean.Movie
import java.io.*

class Utils {
    companion object {
        fun saveImageToDisk(drawableID: Int, context: Context): Boolean {
            val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, drawableID)
            val dataFile = File(context.dataDir.path + File.separator + Constants.DATA_NAME)
            var result = false
            var fileOutputStream: FileOutputStream? = null

            try {
                fileOutputStream = FileOutputStream(dataFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                result = true
            } catch (e: Exception) {
            } finally {
                try {
                    fileOutputStream?.close()
                } catch (e: Exception) {
                }
            }

            return result
        }

        fun readImageToDisk(context: Context): Bitmap? {
            var bitmap: Bitmap? = null

            try {
                val options = BitmapFactory.Options()
                options.inSampleSize = 4
                bitmap = BitmapFactory.decodeFile(
                        context.dataDir.path + File.separator + Constants.DATA_NAME,
                        options)
            } catch (e: Exception) {
            }

            return bitmap
        }

        fun saveInstanceToDisk(movie: Movie, context: Context): Boolean {
            val savedFile = File(context.filesDir.path + File.separator + Constants.FILE_NAME)
            var objectOutputStream: ObjectOutputStream? = null
            var result = false

            try {
                objectOutputStream = ObjectOutputStream(FileOutputStream(savedFile))
                objectOutputStream.writeObject(movie)
                result = true
            } catch (e: IOException) {
            } finally {
                try {
                    objectOutputStream?.close()
                } catch (e: IOException) {
                }
            }

            return result
        }

        fun readInstanceFromDisk(context: Context): Movie? {
            val savedFile = File(context.filesDir.path + File.separator + Constants.FILE_NAME)
            var objectInputStream: ObjectInputStream? = null
            var movie: Movie? = null

            try {
                objectInputStream = ObjectInputStream(FileInputStream(savedFile))
                movie = objectInputStream.readObject() as Movie
            } catch (e: Exception) {
            } finally {
                try {
                    objectInputStream?.close()
                } catch (e: IOException) {}
            }

            return movie
        }
    }
}