package com.loufei.androidx_skin

import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.lang.Exception

/**
 * Created by lvtengfei on 2019-11-21.
 */
data class SkinItem(val view: View, val attrList: ArrayList<SkinAttr>) {

    fun applySkin() {
        try {
            attrList.forEach { attr ->
                if (TextUtils.equals(attr.attributeName, "background")) {
                    if (TextUtils.equals(attr.attrType, "color")) {
                        SkinManager.getColor(attr.resId)?.let {
                            view.setBackgroundColor(it)
                        }
                    } else {
                        SkinManager.getDrawable(attr.resId)?.let {
                            view.setBackgroundDrawable(it)
                        }
                    }
                }

                if(attr.attributeName=="backgroundTint"){
                    SkinManager.getColorStatList(attr.resId)?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            view.backgroundTintList = it
                        }
                    }
                }

                if (view is TextView){
                    SkinManager.getColor(attr.resId)?.takeIf { attr.attributeName=="textColor" }?.let {
                        view.setTextColor(it)
                    }
                }
                if (view is ImageView){
                    SkinManager.getDrawable(attr.resId)?.takeIf { attr.attributeName=="src" }?.let {
                        view.setImageDrawable(it)
                    }
                }
            }
        }catch (e:Exception){
            Log.e("","资源无效")
        }

    }
}