package com.loufei.androidx_skin.strategy

import android.content.Context
import com.loufei.androidx_skin.SkinManager
import java.io.File
import java.io.FileOutputStream

/**
 * Created by lvtengfei on 2019-11-29.
 */
class SkinAssetsLoader : SkinSDCardLoader() {


    override fun getSkinPath(context: Context, skinName: String): String {
        return copyFromByAssets(context, skinName)
    }

    override fun getType() = SkinManager.SKIN_LOAD_STRATEGY_ASSETS

    private fun copyFromByAssets(context: Context, skinName: String): String {
        val file = File(SkinManager.getSDSkinPath(skinName))

        val inputStream = context.assets.open(skinName)
        val outputStream = FileOutputStream(file)
        val byteArray = ByteArray(1024)
        var readLen = inputStream.read(byteArray)
        while (readLen!=-1){
            outputStream.write(byteArray,0,readLen)
            readLen = inputStream.read(byteArray)
        }
        outputStream.close()
        inputStream.close()

        return file.absolutePath
    }
}