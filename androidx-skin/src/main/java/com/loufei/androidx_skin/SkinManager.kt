package com.loufei.androidx_skin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.AttributeSet
import android.util.Log
import androidx.collection.ArrayMap
import com.loufei.androidx_skin.strategy.*
import kotlinx.coroutines.*
import java.io.File

/**
 * Created by lvtengfei on 2019-11-25.
 */
@SuppressLint("StaticFieldLeak")
object SkinManager {

    const val SKIN_LOAD_STRATEGY_DEFAULT = -1
    const val SKIN_LOAD_STRATEGY_ASSETS = 0
    const val SKIN_LOAD_STRATEGY_RES = 1
    const val SKIN_LOAD_STRATEGY_SD = 2

    private val mObserverMap = ArrayMap<Activity, SkinSupportListener>()
    private lateinit var mContext: Context

    private val mStrategyMap = ArrayMap<Int, SkinLoadStrategy>()


    fun addSkinObserver(activity: Activity, layoutInflater: SkinLayoutInflater) {
        mObserverMap[activity] = object : SkinSupportListener {
            override fun onSkinUpdate() {
                Log.d("addSkinObserver","onSkinUpdate")
                layoutInflater.changeSkin()
            }
        }
    }

    fun removeObserver(activity: Activity) {
        takeIf { mObserverMap.contains(activity) }?.apply {
            mObserverMap.remove(activity)
        }
    }

    fun init(application: Application) {
        mContext = application
        application.registerActivityLifecycleCallbacks(SkinActivityLifecycleCallback())
        loadStrategy()
        SkinPreference.init(mContext)
    }

    private fun loadStrategy(){
        mStrategyMap[SKIN_LOAD_STRATEGY_ASSETS] = SkinAssetsLoader()
        mStrategyMap[SKIN_LOAD_STRATEGY_RES] = SkinResLoader()
        mStrategyMap[SKIN_LOAD_STRATEGY_SD] = SkinSDCardLoader()
        mStrategyMap[SKIN_LOAD_STRATEGY_DEFAULT] = SkinDefaultLoader()
    }

    /**
     * 加载皮肤
     */
    fun loadSkin(skinName:String,strategy: Int){
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val block: suspend CoroutineScope.() -> Unit = {
            val background =
                mStrategyMap[strategy]?.loadSkinInBackground(mContext, skinName)
            withContext(Dispatchers.Main) {
                SkinPreference.setSkinName(skinName).setSkinStrategy(strategy).commitEditor()
                changeSkin()
                cancel()
            }
        }
        val job = coroutineScope.launch(block = block)
    }
    //为某一个view进行换肤
    fun loadSkinForSingleView(skinItems:ArrayList<SkinItem>,skinName:String,strategy: Int){
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val block: suspend CoroutineScope.() -> Unit = {
            val background =
                mStrategyMap[strategy]?.loadSkinInBackground(mContext, skinName)
            withContext(Dispatchers.Main) {
                SkinPreference.setSkinName(skinName).setSkinStrategy(strategy).commitEditor()
                skinItems.forEach {
                    it.applySkin()
                    if ((it.view is SkinSupportListener)) {
                        it.view.onSkinUpdate()
                    }
                }
                cancel()
            }
        }
        val job = coroutineScope.launch(block = block)
    }

    fun getColor(resId: Int) = SkinResources.getColor(mContext,resId)
    fun getColorStatList(resId: Int) = SkinResources.getColorStatList(mContext,resId)


    fun getDrawable(resId: Int) = SkinResources.getDrawable(mContext,resId)

    fun restoreDefault() {
        loadSkin("", SKIN_LOAD_STRATEGY_DEFAULT)
    }

    private fun changeSkin() {
        mObserverMap.keys.forEach {
            Log.d("mObserverMap","$it")
            mObserverMap[it]?.onSkinUpdate()
        }
    }


    fun getSkinPkgName(skinPath: String): String {
        mContext.packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
            ?.let {
                return it.packageName
            }
        return mContext.packageName
    }

    @Deprecated("")
    fun getSkinResource(skinPath: String): Resources {

        val packageArchiveInfo = mContext.packageManager.getPackageArchiveInfo(skinPath, 0)
        packageArchiveInfo?.let {
            it.applicationInfo.sourceDir = skinPath
            it.applicationInfo.publicSourceDir = skinPath
            val resources =
                mContext.packageManager.getResourcesForApplication(it.applicationInfo)
            return Resources(
                resources.assets,
                mContext.resources.displayMetrics,
                mContext.resources.configuration
            )
        }
        return mContext.resources
    }

    //获取下载皮肤的路径
    fun getSDSkinPath(skinName: String): String {
        File(SkinFileUtils.getSkinDir(mContext),skinName).createNewFile()
        return File(SkinFileUtils.getSkinDir(mContext),skinName).absolutePath
    }

    //获取自定义view中属性对应的id
    fun getCustomViewAttrResId(attributeSet: AttributeSet,supportAttrName:String): Int {
        attributeSet.attributeCount.downTo(1).forEach { index ->
            val attributeName = attributeSet.getAttributeName(index - 1)
            val attributeValue = attributeSet.getAttributeValue(index - 1)
            attributeName?.takeIf { it == supportAttrName }?.let {
                return attributeValue.substring(1).toInt()
            }
        }
        return 0
    }

}