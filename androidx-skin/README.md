### 用法

1.在Application中初始化

`class SkinApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }

}`

2.目前View中设置了background，backgroundColor，textColor属性时，都是支持换肤的

3.加载皮肤路径

`SkinManager.loadSkinPath("皮肤路径")`

4.使用皮肤

`SkinManager.changeSkin()`

5.使用默认皮肤

`SkinManager.restoreDefault()`
