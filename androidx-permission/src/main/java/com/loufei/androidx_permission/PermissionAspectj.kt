package com.loufei.androidx_permission

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.loufei.androidx_permission.annotation.PermissionCanceled
import com.loufei.androidx_permission.annotation.PermissionDenied
import com.loufei.androidx_permission.annotation.Permissions
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * Created by lvtengfei on 2019-11-27.
 */
@Aspect
class PermissionAspectj {

    @Pointcut("execution(@com.loufei.androidx_permission.annotation.Permissions * *(..)) && @annotation(permissions)")
    fun requestPermission(permissions:Permissions){
        Log.e("requestPermission","拦截")
    }

    @Around("requestPermission(permissions)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint,permissions: Permissions){
        var context:Context? = null
        when(val any = joinPoint.`this`){
            is Context -> context = any
            is Fragment -> context = any.requireContext()
            is android.app.Fragment -> context = any.activity
            is View -> context = any.context
            is Activity -> context = any
        }

        Log.e("requestPermission","aroundJoinPoint")

        context?.let { context1 ->
            if (PermissionManager.hasPermissions(context1,permissions.permissions)){
                joinPoint.proceed()
            }else{
                PermissionActivity.requestPermissions(context1,permissions.permissions,object :IPermission{
                    override fun onGranted() {
                        joinPoint.proceed()
                    }

                    override fun onDenied(deniedPermission: Array<String>) {
                        val javaClass = joinPoint.`this`.javaClass
                        val declaredMethods = javaClass.declaredMethods
                        declaredMethods.forEach {
                            it.takeIf { it.isAnnotationPresent(PermissionDenied::class.java) }?.apply {
                                it.isAccessible = true
                                it.invoke(joinPoint.`this`)
                            }
                        }
                    }

                    override fun onCanceled() {
                        val javaClass = joinPoint.`this`.javaClass
                        val declaredMethods = javaClass.declaredMethods
                        declaredMethods.forEach {
                            it.takeIf { it.isAnnotationPresent(PermissionCanceled::class.java) }?.apply {
                                it.isAccessible = true
                                it.invoke(joinPoint.`this`)
                            }
                        }
                    }
                })
            }
        }
    }
}