### 用法

#### 支持sd卡，应用内，assets换肤

1. 添加引用

        implementation 'com.loufei.thridlib:androidx-skin:0.0.8'

2. 在Application中初始化

        class SkinApplication:Application() {
        
            override fun onCreate() {
                super.onCreate()
                SkinManager.init(this)
            }
        
        }

3. 目前View中设置了background，backgroundColor，textColor属性时，都是支持换肤的

4. 自定义View需实现SkinSupportListener接口，才可支持换肤，可以参考 CustomView的实现

5. 加载皮肤
        
        SkinManager.loadSkin("skinName",SkinManager.SKIN_LOAD_STRATEGY_SD)

    skinName是资源包的名称

6. 使用默认皮肤

        SkinManager.restoreDefault()
        
7. 保存从服务端下载的皮肤时，需要使用
        
        SkinManager.getSDSkinPath("skinName")
        
    获取到路径，然后在进行保存操作
