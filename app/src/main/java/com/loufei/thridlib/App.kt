package com.loufei.thridlib

import android.app.Application
import com.loufei.androidx_skin.SkinManager

/**
 * Created by lvtengfei on 2019-12-02.
 */
class App :Application(){

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}