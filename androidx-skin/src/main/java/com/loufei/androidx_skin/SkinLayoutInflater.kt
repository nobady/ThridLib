package com.loufei.androidx_skin

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.collection.ArrayMap
import java.lang.reflect.Constructor

/**
 * Created by lvtengfei on 2019-11-25.
 */
class SkinLayoutInflater:LayoutInflater.Factory2 {

    private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
    var mActivityCompatDelegate:AppCompatDelegate? = null
    //如果是创建viewGroup，需要自行将前缀补上，否则会抛异常
    private val sClassPrefixList = arrayOf("android.widget.", "android.view.", "android.webkit.")
    //缓存构造参数，参考系统设计
    private val sConstructorMap = ArrayMap<String,Constructor<out View>>()
    //缓存所有的需要换肤的view及其属性
    private val mSkinItems= ArrayList<SkinItem>()

    override fun onCreateView(parent: View?, name: String, context: Context, attr: AttributeSet): View? {
        //使用系统的方法进行创建view
        var createView = mActivityCompatDelegate?.createView(parent, name, context, attr)
        //如果是自定义view或者是系统viewGroup，走这里
        createView?:apply {
            //如果不包含.说明是系统的view，就需要自己增加前缀
            createView = if (-1==name.indexOf(".")){
                createViews(context,name,sClassPrefixList,attr)
            }else{
                createViews(context,name,null,attr)
            }
        }

        createView?.let {
            collectionAttrs(it,context,attr)
        }

        return createView
    }

    private fun collectionAttrs(view: View, context: Context, attr: AttributeSet) {
        val attrList = ArrayList<SkinAttr>()

        attr.attributeCount.downTo(1).forEach { index ->
            val attributeName = attr.getAttributeName(index-1)
            val attributeValue = attr.getAttributeValue(index - 1)
            attributeName.takeIf { isSupportSkin(it) }?.let {
                //必须是以@开头的属性
                takeIf { attributeValue.startsWith("@") }?.apply {
                    val resId = attributeValue.substring(1).toInt()
                    val resName = context.resources.getResourceEntryName(resId)
                    val resType = context.resources.getResourceTypeName(resId)
                    attrList.add(SkinAttr(attributeName,resType,resName,resId))
                }
            }
        }
        mSkinItems.add(SkinItem(view,attrList))
    }

    private fun isSupportSkin(attrName:String) = TextUtils.equals(attrName,"background")
            ||TextUtils.equals(attrName,"textColor")


    private fun createViews(
        context: Context,
        name: String,
        sClassPrefixList: Array<String>?,
        attr: AttributeSet
    ): View? {
        var constructor = sConstructorMap[name]
        var clazz:Class<out View>? = null

        constructor?:apply {
            //如果没有这个构建函数，就重新创建并缓存起来
            sClassPrefixList?.let { prefixs ->
                prefixs.run {
                    prefixs.forEach {
                        clazz = context.classLoader.loadClass("$it$name")
                            .asSubclass(View::class.java) as Class<out View>?
                        //类似于break
                        clazz?.apply { return@run }
                    }
                }
            }
            sClassPrefixList?:apply{
                clazz = context.classLoader.loadClass(name)
                    .asSubclass(View::class.java) as Class<out View>?
            }

            constructor = clazz?.getConstructor(*sConstructorSignature)
            constructor?.isAccessible = true
            sConstructorMap[name] = constructor
        }

        return constructor?.newInstance(context,attr)
    }

    override fun onCreateView(p0: String, p1: Context, p2: AttributeSet): View? {
        return null
    }

    fun changeSkin() {
        mSkinItems.forEach {
            it.applySkin()
        }
    }
}