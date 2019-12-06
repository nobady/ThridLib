package com.loufei.androidx_skin

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by lvtengfei on 2019-11-28.
 */
object SkinPreference {

    private const val FILE_NAME="skin_meta_data"
    private const val KEY_SKIN_NAME="skin_name"
    private const val KEY_SKIN_STRATEGY="skin_strategy"
    private lateinit var mSharedPreferences:SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    fun init(context: Context){
        mSharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
        mEditor = mSharedPreferences.edit()
    }

    fun setSkinName(skinName:String): SkinPreference {
        mEditor.putString(KEY_SKIN_NAME,skinName)
        return this
    }

    fun getSkinName() =
        mSharedPreferences.getString(KEY_SKIN_NAME,"")

    fun setSkinStrategy(strategy:Int): SkinPreference {
        mEditor.putInt(KEY_SKIN_STRATEGY,strategy)
        return this
    }

    fun getSkinStrategy() = mSharedPreferences.getInt(KEY_SKIN_STRATEGY,SkinManager.SKIN_LOAD_STRATEGY_DEFAULT)

    fun commitEditor(){
        mEditor.apply()
    }
}