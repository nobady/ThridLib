package com.loufei.androidx_skin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import androidx.collection.ArrayMap
import androidx.core.content.ContextCompat
import java.io.File
import java.lang.Exception

/**
 * Created by lvtengfei on 2019-11-25.
 */
@SuppressLint("StaticFieldLeak")
object SkinManager {

    private val mObserverMap = ArrayMap<Activity,SkinUpdateListener>()
    private var mContext:Context? = null
    private var mSkinPackName:String? = ""
    private var mOutResources:Resources? = null
    fun addSkinObserver(activity: Activity,layoutInflater: SkinLayoutInflater){
        mObserverMap[activity] = object :SkinUpdateListener{
            override fun onSkinUpdate() {
                layoutInflater.changeSkin()
            }
        }
    }

    fun removeObserver(activity: Activity){
        takeIf { mObserverMap.contains(activity) }?.apply {
            mObserverMap.remove(activity)
        }
    }

    fun init(application: Application){
        mContext = application
        application.registerActivityLifecycleCallbacks(SkinActivityLifecycleCallback())
    }

    fun loadSkinPath(skinPath:String){
        val file = File(skinPath)
        takeIf { !file.exists() }?.apply { return }

        try {
            mContext?.let {
                val packageManager = it.packageManager
                val packageInfo =
                    packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                mSkinPackName = packageInfo?.packageName
                //通过反射加载外部的资源包
                val assetManager = AssetManager::class.java.newInstance()
                val method = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                method.isAccessible = true
                method.invoke(assetManager,skinPath)
                //构建一个Resources对象来加载皮肤包中的资源，参考系统加载资源的实现方式
                mOutResources = Resources(assetManager, mContext?.resources?.displayMetrics,
                    mContext?.resources?.configuration)
            }
        }catch (e:Exception){
            Log.e("SkinManager","加载皮肤时出异常，异常为：${e.message}")
        }
    }

    fun getColor(resName:String,resId:Int): Int? {
        mOutResources?:return mContext?.resources?.getColor(resId)

        val id = mOutResources?.getIdentifier(resName, "color", mSkinPackName)
        takeIf { id==0 }?.apply { return resId }
        return mOutResources?.getColor(id!!)
    }

    fun getDrawable(resName:String,resId:Int):Drawable?{
        mContext?.let {
            mOutResources?:return ContextCompat.getDrawable(it,resId)
            val id = mOutResources?.getIdentifier(resName, "background", mSkinPackName)
            takeIf { id==0 }?.apply { return ContextCompat.getDrawable(it,resId) }

            return mOutResources?.getDrawable(id!!)
        }
       throw Exception("请先调用init方法进行初始化")
    }

    fun restoreDefault(){
        mOutResources = null
        changeSkin()
    }
    

    fun changeSkin() {
        mObserverMap.keys.forEach {
            mObserverMap[it]?.onSkinUpdate()
        }
    }

}