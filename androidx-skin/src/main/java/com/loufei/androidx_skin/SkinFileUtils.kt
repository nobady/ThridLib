package com.loufei.androidx_skin

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import androidx.core.os.EnvironmentCompat
import java.io.File

/**
 * Created by lvtengfei on 2019-11-28.
 */
object SkinFileUtils {


    fun getSkinDir(context: Context): String {
        val file = File(getCacheDir(context), "loufei_skins")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    private fun getCacheDir(context: Context): String {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val cacheDir = context.externalCacheDir
            cacheDir?.let {
                if ((it.exists()||it.mkdirs())) {
                    return it.absolutePath
                }
            }
        }
        return context.cacheDir.absolutePath
    }

    fun isFileExists(path:String): Boolean {
        return (!TextUtils.isEmpty(path))&&File(path).exists()
    }
}