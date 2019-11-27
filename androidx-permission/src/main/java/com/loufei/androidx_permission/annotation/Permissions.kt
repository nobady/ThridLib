package com.loufei.androidx_permission.annotation

/**
 * Created by lvtengfei on 2019-11-27.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Permissions(val permissions:Array<String>)