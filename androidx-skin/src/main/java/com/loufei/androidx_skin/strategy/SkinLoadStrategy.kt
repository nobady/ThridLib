package com.loufei.androidx_skin.strategy

import android.content.Context

/**
 * 皮肤加载的策略，分为三种，SD卡，assets，应用内（res下）
 * 参考Android-skin-support的实现  github地址：https://github.com/ximsfei/Android-skin-support.git
 * Created by lvtengfei on 2019-11-27.
 */
interface SkinLoadStrategy {
    /**
     * return 资源名称（比如TextView设置的TextColor属性的值是R.color.blue,那么返回的就是blue）
     * resId:属性值去掉@后的值
     */
    fun getTargetResourceEntryName(context: Context, skinName: String, resId: Int): String

    /**
     * 根据不同的策略，来设置不同的resource，sd卡的策略就需要重新创建一个Resource来加载皮肤资源
     * 应用内的用context.resource就可以来获取
     */
    fun loadSkinInBackground(context: Context,skinName: String):String

    fun getType():Int
}