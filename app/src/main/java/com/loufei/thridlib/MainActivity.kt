package com.loufei.thridlib

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.loufei.androidx_permission.annotation.PermissionCanceled
import com.loufei.androidx_permission.annotation.PermissionDenied
import com.loufei.androidx_permission.annotation.Permissions
import com.loufei.androidx_skin.SkinManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    @Permissions(permissions = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    fun getPermission(view: View) {
        Toast.makeText(this,"权限已获取",Toast.LENGTH_SHORT).show()
    }
    @PermissionCanceled
    fun test(){
        Toast.makeText(this,"权限取消",Toast.LENGTH_SHORT).show()
    }
    @PermissionDenied
    fun test2(){
        Toast.makeText(this,"权限已拒绝",Toast.LENGTH_SHORT).show()
    }
    fun restoreDefault(view: View) {
        SkinManager.restoreDefault()
    }
    fun loadSDSkin(view: View) {
        SkinManager.loadSkin("skin.apk",SkinManager.SKIN_LOAD_STRATEGY_SD)
    }

    fun downloadSkin(view: View) {
        Log.e("downloadSkin",SkinManager.getSDSkinPath("skin.apk.1"))
    }

    fun appSkin(view: View) {
        SkinManager.loadSkin("test",SkinManager.SKIN_LOAD_STRATEGY_RES)
    }
}
