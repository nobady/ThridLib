package com.loufei.androidx_permission

/**
 * Created by lvtengfei on 2019-11-27.
 */
interface IPermission {
    fun onGranted()
    fun onDenied(deniedPermission:Array<String>)
    fun onCanceled()
}