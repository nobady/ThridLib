package com.loufei.androidx_permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Created by lvtengfei on 2019-11-27.
 */
object PermissionManager {

    fun hasPermissions(context: Context, permissions:Array<String>): Boolean {
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(context,it)!= PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun requestPermissions(context: Activity, permissions: Array<String>, requestCode:Int){
        ActivityCompat.requestPermissions(context,permissions,requestCode)
    }

    fun verifyPermissions(granted:IntArray): Boolean {
        granted.forEach {
            if (it!= PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    /**
     * 在拒绝的权限组中，如果勾选了"不在提示",返回ture，反之返回false
     */
    fun shouldShowRequestPermissionRationale(
        activity: Activity,
        permissions: Array<out String>
    ): Boolean {
        permissions.forEach {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,it)) {
                return true
            }
        }
        return false
    }
}