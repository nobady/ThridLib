package com.loufei.androidx_permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by lvtengfei on 2019-11-27.
 */
class PermissionActivity:AppCompatActivity() {

    companion object{
        private var permissionListener:IPermission? = null
        private const val REQUEST_PERMISSIONS_EXTRA = "request_permissions_code"

        fun requestPermissions(context: Context, permissions: Array<out String>, permission:IPermission){

            permissionListener = permission
            val intent = Intent()
            intent.setClass(context,PermissionActivity::class.java)
            intent.putExtra(REQUEST_PERMISSIONS_EXTRA,permissions)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            if (context is Activity){
                context.overridePendingTransition(0,0)
            }
        }

    }
    private var requestCode = 0
    private var permissions = Array(0) {""}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissions = intent.run { getStringArrayExtra(REQUEST_PERMISSIONS_EXTRA) }

        if (PermissionManager.hasPermissions(this,permissions)) {
            permissionListener?.onGranted()
            finish()
        }else{
            PermissionManager.requestPermissions(this,permissions,requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        takeIf { requestCode==this.requestCode }?.run {
            when {
                PermissionManager.verifyPermissions(grantResults) -> {
                    permissionListener?.onGranted()
                    finish()
                }
                PermissionManager.shouldShowRequestPermissionRationale(this,permissions) -> {
                    val deniedArray = Array<String>(0){""}
                    grantResults.forEachIndexed { index, i ->
                        if (i== PackageManager.PERMISSION_DENIED) {
                            deniedArray.plus(permissions[index])
                        }
                    }
                    permissionListener?.onDenied(deniedArray)
                }
                else -> permissionListener?.onCanceled()
            }
            finish()
        }
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(0,0)
        permissionListener = null
    }
}