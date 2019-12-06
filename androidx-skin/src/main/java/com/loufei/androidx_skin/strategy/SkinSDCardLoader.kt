package com.loufei.androidx_skin.strategy

import android.content.Context
import com.loufei.androidx_skin.SkinFileUtils
import com.loufei.androidx_skin.SkinManager
import com.loufei.androidx_skin.SkinResources
import java.io.File

/**
 * 从sd卡中加载
 * Created by lvtengfei on 2019-11-28.
 */
open class SkinSDCardLoader: SkinLoadStrategy {

    override fun getType() = SkinManager.SKIN_LOAD_STRATEGY_SD

    override fun loadSkinInBackground(context: Context, skinName: String): String {
        val skinPath = getSkinPath(context,skinName)
        if (SkinFileUtils.isFileExists(skinPath)) {
            val skinPkgName = SkinManager.getSkinPkgName(skinPath)
            val skinResource = SkinManager.getSkinResource(skinPath)
            SkinResources.setupSkin(skinResource,skinName,skinPkgName,this)
        }
        return skinName
    }

    open fun getSkinPath(context: Context,skinName: String): String {
        return File(SkinFileUtils.getSkinDir(context),skinName).absolutePath
    }

    override fun getTargetResourceEntryName(
        context: Context,
        skinName: String,
        resId: Int
    ): String = context.resources.getResourceEntryName(resId)


}