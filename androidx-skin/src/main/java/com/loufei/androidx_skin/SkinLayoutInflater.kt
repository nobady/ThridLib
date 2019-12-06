package com.loufei.androidx_skin

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.collection.ArrayMap
import java.lang.reflect.Constructor
import kotlin.Exception

/**
 * Created by lvtengfei on 2019-11-25.
 */
class SkinLayoutInflater : LayoutInflater.Factory2 {

    private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
    var mActivityCompatDelegate: AppCompatDelegate? = null
    //如果是创建viewGroup，需要自行将前缀补上，否则会抛异常
    private val sClassPrefixList = arrayOf("android.widget.", "android.view.", "android.webkit.")
    //缓存构造参数，参考系统设计
    private val sConstructorMap = ArrayMap<String, Constructor<out View>>()
    //缓存所有的需要换肤的view及其属性
    private val mSkinItems = ArrayList<SkinItem>()
    //缓存所有需要换肤的自定义view
    private val mCustomSkinViews = ArrayList<View>()

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attr: AttributeSet
    ): View? {
        //使用系统的方法进行创建view
        var createView = mActivityCompatDelegate?.createView(parent, name, context, attr)
        //如果是自定义view或者是系统viewGroup，走这里
        createView ?: apply {
            //如果不包含.说明是系统的view，就需要自己增加前缀
            createView = if (-1 == name.indexOf(".")) {
                createViews(context, name, sClassPrefixList, attr)
            } else {
                createViews(context, name, null, attr)
            }
        }

        createView?.let {
            collectionAttrs(it, context, attr)
        }
        Log.e("onCreateView", "$mSkinItems")
        collectionCustomView(createView)

        //这里让当前view更加当前的皮肤包进行加载皮肤
        SkinPreference.getSkinName()
            ?.let { SkinManager.loadSkinForSingleView(mSkinItems,it, SkinPreference.getSkinStrategy()) }

        return createView
    }

    private fun collectionCustomView(view: View?) {
        view?.takeIf { it is SkinSupportListener }?.apply {
            Log.e("collectionCustomView", "$view")
            mCustomSkinViews.add(view)
        }
    }

    private fun collectionAttrs(view: View, context: Context, attr: AttributeSet) {
        val attrList = ArrayList<SkinAttr>()

        attr.attributeCount.downTo(1).forEach { index ->
            val attributeName = attr.getAttributeName(index - 1)
            val attributeValue = attr.getAttributeValue(index - 1)
            attributeName.takeIf { AttrFactoryImpl.isSupportAttr(it) }?.let {
                //必须是以@开头的属性
                takeIf { attributeValue.startsWith("@") }?.apply {
                    val resId = attributeValue.substring(1).toInt()
                    val resName = context.resources.getResourceEntryName(resId)
                    val resType = context.resources.getResourceTypeName(resId)
                    val skinAttr = SkinAttr(attributeName, resType, resName, resId)
                    Log.e("collectionAttrs", "$skinAttr")
                    attrList.add(skinAttr)
                }
            }
        }
        takeIf { attrList.isNotEmpty() }?.run { mSkinItems.add(SkinItem(view, attrList)) }
    }


    private fun createViews(
        context: Context,
        name: String,
        sClassPrefixList: Array<String>?,
        attr: AttributeSet
    ): View? {
        var constructor = sConstructorMap[name]
        var clazz: Class<out View>? = null

        constructor ?: apply {
            //如果没有这个构建函数，就重新创建并缓存起来
            sClassPrefixList?.let { prefixs ->
                prefixs.run {
                    prefixs.forEach {
                        try {
                            clazz = context.classLoader.loadClass("$it$name")
                                .asSubclass(View::class.java) as Class<out View>?
                            //类似于break
                            clazz?.apply { return@run }
                        } catch (e: Exception) {

                        }
                    }
                }
            }
            sClassPrefixList ?: apply {
                try {
                    clazz = context.classLoader.loadClass(name)
                        .asSubclass(View::class.java) as Class<out View>?
                } catch (e: Exception) {

                }
            }

            constructor = clazz?.getConstructor(*sConstructorSignature)
            constructor?.isAccessible = true
            sConstructorMap[name] = constructor
        }

        return constructor?.newInstance(context, attr)
    }

    override fun onCreateView(p0: String, p1: Context, p2: AttributeSet): View? {
        return null
    }

    fun changeSkin() {
        mSkinItems.forEach {
            it.applySkin()
        }

        mCustomSkinViews.forEach { (it as SkinSupportListener).onSkinUpdate() }
    }
}