package com.loufei.androidx_skin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import com.loufei.androidx_skin.strategy.SkinLoadStrategy

/**
 * 管理皮肤加载的resource
 * Created by lvtengfei on 2019-11-28.
 */
@SuppressLint("StaticFieldLeak")
object SkinResources {

    private var mResources:Resources?=null
    private var mSkinName:String = ""
    private var mSkinPkgName = ""
    private var mStrategy: SkinLoadStrategy? = null


    fun setupSkin(resources: Resources,skinName:String,skinPkgName:String,strategy: SkinLoadStrategy){
        mResources = resources
        mSkinName = skinName
        mSkinPkgName = skinPkgName
        mStrategy = strategy
    }

    fun getColor(context: Context,resId:Int): Int? {
        if(resId<=0){
            return null
        }
        val resName =
            mStrategy?.getTargetResourceEntryName(context, mSkinName, resId)
        val typeName = context.resources.getResourceTypeName(resId)
        val identifier = mResources?.getIdentifier(resName, typeName, mSkinPkgName)
        identifier?.let {
            if (it>0){
                return mResources?.getColor(it)
            }
        }
        return context.resources.getColor(resId)
    }

    fun getDrawable(context: Context, resId: Int): Drawable? {
        if(resId<=0){
            return null
        }
        val resName =
            mStrategy?.getTargetResourceEntryName(context, mSkinName, resId)
        val typeName = context.resources.getResourceTypeName(resId)
        val identifier = mResources?.getIdentifier(resName, typeName, mSkinPkgName)
        identifier?.let {
            if (it>0){
                return mResources?.getDrawable(identifier)
            }
        }
        return ContextCompat.getDrawable(context,resId)
    }

    fun getColorStatList(context: Context, resId: Int): ColorStateList? {
        if(resId<=0){
            return null
        }
        val resName =
            mStrategy?.getTargetResourceEntryName(context, mSkinName, resId)
        val typeName = context.resources.getResourceTypeName(resId)
        val identifier = mResources?.getIdentifier(resName, typeName, mSkinPkgName)
        identifier?.let {
            if (it>0){
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mResources?.getColorStateList(it,context.theme)
                } else {
                    mResources?.getColorStateList(it)
                }
            }
        }
        return context.resources.getColorStateList(resId)
    }



}