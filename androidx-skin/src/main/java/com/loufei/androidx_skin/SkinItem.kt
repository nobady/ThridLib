package com.loufei.androidx_skin

import android.text.TextUtils
import android.view.View
import android.widget.TextView

/**
 * Created by lvtengfei on 2019-11-21.
 */
data class SkinItem(val view: View, val attrList: ArrayList<SkinAttr>) {

    fun applySkin() {
        attrList.forEach { attr ->
            if (TextUtils.equals(attr.attributeName, "background")) {
                if (TextUtils.equals(attr.attrType, "color")) {
                    SkinManager.getColor(attr.resName, attr.resId)?.let {
                        view.setBackgroundColor(it)
                    }
                } else {
                    SkinManager.getDrawable(attr.resName, attr.resId)?.let {
                        view.setBackgroundDrawable(it)
                    }
                }
            }

            if (view is TextView) {
                if (TextUtils.equals(attr.attributeName, "textColor")) {
                    SkinManager.getColor(attr.resName, attr.resId)?.let { view.setTextColor(it) }
                }
            }
        }
    }
}