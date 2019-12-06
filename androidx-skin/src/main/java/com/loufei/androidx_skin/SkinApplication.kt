package com.loufei.androidx_skin

import android.app.Application

/**
 * Created by lvtengfei on 2019-11-25.
 */
class SkinApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }

}