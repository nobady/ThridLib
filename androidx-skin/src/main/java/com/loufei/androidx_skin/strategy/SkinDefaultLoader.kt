package com.loufei.androidx_skin.strategy

import android.content.Context
import com.loufei.androidx_skin.SkinManager
import com.loufei.androidx_skin.SkinResources

/**
 * Created by lvtengfei on 2019-12-02.
 */
class SkinDefaultLoader: SkinLoadStrategy {

    override fun getTargetResourceEntryName(
        context: Context,
        skinName: String,
        resId: Int
    ): String = context.resources.getResourceEntryName(resId)

    override fun loadSkinInBackground(context: Context, skinName: String): String {
        SkinResources.setupSkin(context.resources,skinName,context.packageName,this)
        return skinName
    }

    override fun getType() = SkinManager.SKIN_LOAD_STRATEGY_DEFAULT
}