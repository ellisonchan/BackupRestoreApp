package com.ellison.backupdemo.bean

import android.graphics.Bitmap

data class CacheData(var dataImage: Bitmap?,
                     var serializedMovie: Movie?,
                     var dbMovie: Movie?)