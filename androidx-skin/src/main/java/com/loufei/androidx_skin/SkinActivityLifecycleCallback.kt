package com.loufei.androidx_skin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 所有的act生命周期都会在这个类中显示，所以在act启动的时候进行切入，需要进行切换皮肤的view将由我们自己定义实现
 * Created by lvtengfei on 2019-11-25.
 */
class SkinActivityLifecycleCallback:Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityDestroyed(p0: Activity) {
        SkinManager.removeObserver(p0)
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        activity.layoutInflater.let {
            val field = it.javaClass.superclass?.getDeclaredField("mFactorySet")
            field?.isAccessible = true
            field?.setBoolean(it,false)

            val layoutInflater = SkinLayoutInflater()
            takeIf { activity is AppCompatActivity }?.apply {
                layoutInflater.mActivityCompatDelegate = (activity as AppCompatActivity).delegate
                it.factory2 = layoutInflater

                SkinManager.addSkinObserver(activity,layoutInflater)
//                SkinManager.loadSkin(SkinPreference.getSkinName()!!,SkinPreference.getSkinStrategy())
            }
        }
    }

    override fun onActivityResumed(p0: Activity) {
    }
}