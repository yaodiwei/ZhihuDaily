# [TinkerPatch](http://www.tinkerpatch.com/) 热修复集成


<br /><br />
——————————————————————————————
# tinker 通用使用步骤
#### 1.打个正常包
#### 2.修改 tinkerpatch.gradle 里面的 baseInfo 旧文件路径
#### 3.使用 tinkerPatchDebug 或者 tinkerPatchRelease  （在右边侧边栏，工程名 -> 工程名 -> Tasks ->tinker 下面）
#### 4.发布到tinkerpatch平台
#### 5.点击更新TinkerPatch.with().fetchPatchUpdate(true); 或者 等待时间间隔的更新
#### 6.成功后锁屏或者kill self实现重启应用。

<br /><br /><br />
——————————————————————————————
# tinkerpatch 集成步骤

## 1.Project build.gradle 
```
apply from: "config.gradle"

buildscript {
    dependencies {
        // 暂时不能升级到3.5，因为tinker还没兼容好
        //https://github.com/BuglyDevTeam/Bugly-Android-Demo/issues/174
        classpath 'com.android.tools.build:gradle:3.4.2'
        // TinkerPatch 插件  无需再单独引用tinker的其他库
        classpath "com.tinkerpatch.sdk:tinkerpatch-gradle-plugin:1.2.14"
    }
}
```

## 2.Module build.gradle
```

apply from: './tinkerpatch.gradle'

dependencies {
    implementation "com.android.support:multidex:1.0.3"
    
    // 若使用annotation需要单独引用anno库, 对于tinker的其他库都无需再引用
    implementation 'com.tinkerpatch.sdk:tinkerpatch-android-sdk:1.2.14'
    compileOnly 'com.tinkerpatch.tinker:tinker-android-anno:1.9.14'
    annotationProcessor 'com.tinkerpatch.tinker:tinker-android-anno:1.9.14'
}
```

## 3.tinkerpatch.gradle
```
apply plugin: 'tinkerpatch-support'

/**
 * TODO: 请按自己的需求修改为适应自己工程的参数
 */
def bakPath = file("${buildDir}/bakApk/")
def baseInfo = "app-1.0.0-1011-20-13-14"
def variantName = "debug"

/**
 * 对于插件各参数的详细解析请参考
 * http://tinkerpatch.com/Docs/SDK
 */
tinkerpatchSupport {
    /** 可以在debug的时候关闭 tinkerPatch **/
    /** 当disable tinker的时候需要添加multiDexKeepProguard和proguardFiles,
     这些配置文件本身由tinkerPatch的插件自动添加，当你disable后需要手动添加
     你可以copy本示例中的proguardRules.pro和tinkerMultidexKeep.pro,
     需要你手动修改'tinker.sample.android.app'本示例的包名为你自己的包名, com.xxx前缀的包名不用修改
     **/
    tinkerEnable = true
    reflectApplication = false
    /**
     * 是否开启加固模式，只能在APK将要进行加固时使用，否则会patch失败。
     * 如果只在某个渠道使用了加固，可使用多flavors配置
     **/
    protectedApp = false
    /**
     * 实验功能
     * 补丁是否支持新增 Activity (新增Activity的exported属性必须为false)
     **/
    supportComponent = true

    autoBackupApkPath = "${bakPath}"

    appKey = "8e54dbd7a1cc1b35"

    /** 注意: 若发布新的全量包, appVersion一定要更新 **/
    appVersion = "1.0.0"

    def pathPrefix = "${bakPath}/${baseInfo}/${variantName}/"
    def name = "${project.name}-${variantName}"

    baseApkFile = "${pathPrefix}/${name}.apk"
    baseProguardMappingFile = "${pathPrefix}/${name}-mapping.txt"
    baseResourceRFile = "${pathPrefix}/${name}-R.txt"

    /**
     *  若有编译多flavors需求, 可以参照： https://github.com/TinkerPatch/tinkerpatch-flavors-sample
     *  注意: 除非你不同的flavor代码是不一样的,不然建议采用zip comment或者文件方式生成渠道信息（相关工具：walle 或者 packer-ng）
     **/
}

/**
 * 用于用户在代码中判断tinkerPatch是否被使能
 */
android {
    defaultConfig {
        buildConfigField "boolean", "TINKER_ENABLE", "${tinkerpatchSupport.tinkerEnable}"
    }
}

/**
 * 一般来说,我们无需对下面的参数做任何的修改
 * 对于各参数的详细介绍请参考:
 * https://github.com/Tencent/tinker/wiki/Tinker-%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97
 */
tinkerPatch {
    ignoreWarning = false
    useSign = true
    dex {
        dexMode = "jar"
        pattern = ["classes*.dex"]
        loader = []
    }
    lib {
        pattern = ["lib/*/*.so"]
    }

    res {
        pattern = ["res/*", "r/*", "assets/*", "resources.arsc", "AndroidManifest.xml"]
        ignoreChange = []
        largeModSize = 100
    }

    packageConfig {
    }
    sevenZip {
        zipArtifact = "com.tencent.mm:SevenZip:1.1.10"
//        path = "/usr/local/bin/7za"
    }
    buildConfig {
        keepDexApply = false
    }
}
```

## 4.Application 类的修改
参考[SampleApplicationLike](https://github.com/TinkerPatch/tinkerpatch-sample/blob/master/app/src/main/java/tinker/sample/android/app/SampleApplicationLike.java)写自己的ApplicationLike

其他的与tinker集成类似
[Tinker 自定义扩展](https://github.com/Tencent/tinker/wiki/Tinker-自定义扩展)
#### 4.1将我们自己Application类以及它的继承类的所有代码拷贝到自己的ApplicationLike继承类中，例如SampleApplicationLike。你也可以直接将自己的Application改为继承ApplicationLike;
#### 4.2Application的attachBaseContext方法实现要单独移动到onBaseContextAttached中，其他逻辑代码也需要移动过来；
#### 4.3对ApplicationLike中，引用application的地方改成getApplication();
#### 4.4对其他引用Application或者它的静态对象与方法的地方，改成引用ApplicationLike的静态对象与方法；
#### 4.5在注解 DefaultLifeCycle 里面的值 application 写好 application 的包名+类名，build一下，修改 AndroidManifest.xml 的 application 名字为这个；

## 5.AndroidManifest.xml
#### 5.1替换运来的 Application 类
#### 5.2添加一个 tinker service 的注册
```
<application
    android:name=".tinker.SampleApplication"
    ...
    
</application>
```

## 8.代码里使用热修复
#### 重点代码：
```
TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
```

其他代码：
#### 删除 patch 代码
[TinkerPatch Api](http://www.tinkerpatch.com/Docs/api)



<br /><br /><br />
——————————————————————————————
# 注意事项
#### 1.注意要关闭Instant run
#### 2.加载成功后会删除修复包

<br /><br /><br />
——————————————————————————————
# 错误码
https://github.com/Tencent/tinker/wiki/Tinker-%E8%87%AA%E5%AE%9A%E4%B9%89%E6%89%A9%E5%B1%95

<br /><br /><br />
——————————————————————————————
# 快捷工具
adb push /Users/Yao/work/tinker/tinker-sample-android/app/build/outputs/apk/tinkerPatch/debug/patch_signed_7zip.apk /sdcard/Download


<br /><br /><br />
——————————————————————————————
# 小米6x 和 小米9 热修复版本1.9.11修复不成功
https://github.com/Tencent/tinker/issues/1023
```
2019-06-10 13:45:46.801 11965-11965/? I/Tinker.SamplePatchListener: receive a patch file: /storage/emulated/0/patch_signed_7zip.apk, file size:122690
2019-06-10 13:45:46.819 11965-11965/? W/Tinker.PatchInfo: read property failed, e:java.io.FileNotFoundException: /data/user/0/tinker.sample.android/tinker/patch.info (No such file or directory)
2019-06-10 13:45:46.819 11965-11965/? W/Tinker.PatchInfo: read property failed, e:java.io.FileNotFoundException: /data/user/0/tinker.sample.android/tinker/patch.info (No such file or directory)
2019-06-10 13:45:46.821 11965-11965/? W/Tinker.UpgradePatchRetry: onPatchListenerCheck retry file is not exist, just return
2019-06-10 13:45:46.831 11965-11965/? I/Tinker.SamplePatchListener: get platform:all
2019-06-10 13:45:46.836 11965-11965/? I/Tinker.TinkerPatchService: run patch service...
2019-06-10 13:45:46.837 11965-11965/? I/Tinker.TinkerPatchService: jobId of tinker patch service is: 1453143321

2019-06-10 13:49:07.016 4468-4468/tinker.sample.android I/Tinker.SamplePatchListener: receive a patch file: /storage/emulated/0/patch_signed_7zip.apk, file size:122690
2019-06-10 13:49:07.027 4468-4468/tinker.sample.android W/Tinker.PatchInfo: read property failed, e:java.io.FileNotFoundException: /data/user/0/tinker.sample.android/tinker/patch.info (No such file or directory)
2019-06-10 13:49:07.027 4468-4468/tinker.sample.android W/Tinker.PatchInfo: read property failed, e:java.io.FileNotFoundException: /data/user/0/tinker.sample.android/tinker/patch.info (No such file or directory)
2019-06-10 13:49:07.028 4468-4468/tinker.sample.android W/Tinker.UpgradePatchRetry: onPatchListenerCheck retry file is not exist, just return
2019-06-10 13:49:07.034 4468-4468/tinker.sample.android I/Tinker.SamplePatchListener: get platform:all
2019-06-10 13:49:07.037 4468-4468/tinker.sample.android I/Tinker.TinkerPatchService: run patch service...
2019-06-10 13:49:07.038 4468-4468/tinker.sample.android I/Tinker.TinkerPatchService: jobId of tinker patch service is: 1453143321
```