## 说明
 一款基于Aspectj的Androidx权限申请框架
 
#### 用法

1. 在项目根目录下的build.gradle文件中添加Aspectj的引用

 `dependencies {
 
        /** 省略其他代码*/
        
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.4'
   
    }`
    
2. 在主module的build.gradle文件中添加Aspectj插件

    `apply plugin: 'android-aspectjx'`

3. 申请权限，比如一个拍照的功能，需要申请相机的权限
    
   ` @Permissions(permissions = [Manifest.permission.CAMERA])
   
    fun takePhoto(){
        //省略代码
    }`
    
    `@PermissionCanceled
    fun onCancel(){
        //权限取消时执行的逻辑
    }
    
    @PermissionDenied
    fun onDenied(){
        //权限拒绝时执行的逻辑
    }
    `

    
