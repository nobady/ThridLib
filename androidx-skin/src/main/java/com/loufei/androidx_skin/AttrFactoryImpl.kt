package com.loufei.androidx_skin

import android.text.TextUtils

/**
 * Created by lvtengfei on 2019-11-29.
 */
object AttrFactoryImpl : AttrFactory {

    override fun isSupportAttr(attrName: String): Boolean {
        return TextUtils.equals(attrName, "background") ||
                TextUtils.equals(attrName, "textColor") ||
                TextUtils.equals(attrName, "src") ||
                TextUtils.equals(attrName, "backgroundTint") ||
                TextUtils.equals(attrName, "progressDrawable") ||
                TextUtils.equals(attrName, "progressBackgroundTint") ||
                TextUtils.equals(attrName, "progressTint") ||
                TextUtils.equals(attrName, "secondaryProgressTint")
    }
}