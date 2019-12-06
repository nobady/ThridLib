package com.loufei.thridlib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.loufei.androidx_skin.SkinManager
import com.loufei.androidx_skin.SkinSupportListener

/**
 * Created by lvtengfei on 2019-12-04.
 */
class CustomView : TextView, SkinSupportListener {

    private var mAttributeSet: AttributeSet? = null
    private var customViewAttrResId: Int = 0

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        mAttributeSet = attributeSet
        val attributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomView)
        val color = attributes.getColor(
            R.styleable.CustomView_textColors,
            resources.getColor(R.color.colorPrimary)
        )
        //必须在recycle之前获取，否则会导致获取的id无效
        customViewAttrResId = SkinManager.getCustomViewAttrResId(attributeSet, "textColors")
        attributes.recycle()

        setTextColor(color)
    }


    override fun onSkinUpdate() {
        mAttributeSet?.let {
            takeIf { customViewAttrResId > 0 }?.run {
                SkinManager.getColor(customViewAttrResId)?.apply {
                    setTextColor(this)
                }
            }
        }

    }

}