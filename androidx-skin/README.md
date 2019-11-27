### 用法

1. 添加引用

        implementation 'com.loufei.thridlib:androidx-skin:0.0.6'

2. 在Application中初始化

        class SkinApplication:Application() {
        
            override fun onCreate() {
                super.onCreate()
                SkinManager.init(this)
            }
        
        }

3. 目前View中设置了background，backgroundColor，textColor属性时，都是支持换肤的

4. 加载皮肤路径

        SkinManager.loadSkinPath("皮肤路径")

5. 使用皮肤

        SkinManager.changeSkin()

6. 使用默认皮肤

        SkinManager.restoreDefault()
