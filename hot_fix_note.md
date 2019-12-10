# Tinker 热修复集成

<br /><br />
## 不兼容 build:gradle:3.5.x 问题 
升级到3.5，会导致tinker不可用，tinker还没兼容好
https://github.com/BuglyDevTeam/Bugly-Android-Demo/issues/174
        

<br /><br />
——————————————————————————————
# tinker 通用使用步骤
#### 1.打个正常包
#### 2.修改 tinker.gradle 里面的旧文件路径
#### 3.使用 tinkerPatchDebug 或者 tinkerPatchRelease  （在右边侧边栏，工程名 -> 工程名 -> Tasks ->tinker 下面）
#### 4.把文件 app -> build -> outputs -> apk -> tinkerPatch -> debug/release -> patch_signed_7zip.apk 推送到手机sdcard里
#### 5.点击加载 patch 执行热更新功能，成功后锁屏或者kill self实现重启应用。

<br /><br /><br />
——————————————————————————————
# tinker 集成步骤
## 1.如果 TinkerID 使用版本号 versionCode 的值，需要增加 project 路径里增加一个 config.gradle 文件，把版本号提取到 config.gradle 里配置
```
ext {
    //版本号
    versions = [
            applicationId           : "com.yao.zhihudaily",
            versionCode             : 1,
            versionName             : "1.0.0",

            compileSdkVersion       : 28,
            minSdkVersion           : 21,
            targetSdkVersion        : 27,

            android_support         : "28.0.0",
    ]
}
```

## 2.Project build.gradle 
```
apply from: "config.gradle"

buildscript {
    dependencies {
        // 暂时不能升级到3.5，因为tinker还没兼容好
        //https://github.com/BuglyDevTeam/Bugly-Android-Demo/issues/174
        classpath 'com.android.tools.build:gradle:3.4.2'
        classpath 'com.tencent.tinker:tinker-patch-gradle-plugin:1.9.14'
    }
}
```

## 3.Module build.gradle
```
apply plugin: 'com.tencent.tinker.patch'

android {
    defaultConfig {
        versionCode rootProject.ext.versions.versionCode
        versionName rootProject.ext.versions.versionName
    }
}

apply from: './tinker.gradle'

dependencies {
    implementation "com.android.support:multidex:1.0.3"
    
    implementation 'com.tencent.tinker:tinker-android-lib:1.9.14'
    compileOnly 'com.tencent.tinker:tinker-android-anno:1.9.14'
    //不加这句依赖，会报下面这个错
    //Annotation processors must be explicitly declared now.  The following dependencies on the compile classpath are found to contain annotation processor.  Please add them to the annotationProcessor configuration.
    //- tinker-android-anno-1.9.14.jar (com.tencent.tinker:tinker-android-anno:1.9.14)
    annotationProcessor 'com.tencent.tinker:tinker-android-anno:1.9.14'
}
```

## 4.tinker.gradle
```

def gitSha() {
    try {
        String gitRev = 'git rev-parse --short HEAD'.execute(null, project.rootDir).text.trim()
        if (gitRev == null) {
            throw new GradleException("can't get git rev, you should add git to system path or just input test value, such as 'testTinkerId'")
        }
        return gitRev
    } catch (Exception e) {
        throw new GradleException("can't get git rev, you should add git to system path or just input test value, such as 'testTinkerId'")
    }
}

android {

    defaultConfig {
        /**
         * buildConfig can change during patch!
         * we can use the newly value when patch
         */
        buildConfigField "String", "MESSAGE", "\"I am the base apk\""
//        buildConfigField "String", "MESSAGE", "\"I am the patch apk\""
        /**
         * client version would update with patch
         * so we can get the newly git version easily!
         */
        buildConfigField "String", "TINKER_ID", "\"${getTinkerIdValue()}\""
        buildConfigField "String", "PLATFORM", "\"all\""
    }

//    aaptOptions{
//        additionalParameters "--emit-ids", "${project.file('public.txt')}"
//        cruncherEnabled false
//    }

//    //use to test flavors support
//    productFlavors {
//        flavor1 {
//            applicationId 'tinker.sample.android.flavor1'
//        }
//
//        flavor2 {
//            applicationId 'tinker.sample.android.flavor2'
//        }
//    }

}

def bakPath = file("${buildDir}/bakApk/")

/**
 * you can use assembleRelease to build you base apk
 * use tinkerPatchRelease -POLD_APK=  -PAPPLY_MAPPING=  -PAPPLY_RESOURCE= to build patch
 * add apk from the build/bakApk
 */
ext {
    //for some reason, you may want to ignore tinkerBuild, such as instant run debug build?
    tinkerEnabled = true

    //for normal build
    //old apk file to build patch apk
    tinkerOldApkPath = "${bakPath}/app-debug-1006-03-03-07.apk"
    //proguard mapping file to build patch apk
    tinkerApplyMappingPath = "${bakPath}/app-debug-1006-03-03-07-mapping.txt"
    //resource R.txt to build patch apk, must input if there is resource changed
    tinkerApplyResourcePath = "${bakPath}/app-debug-1006-03-03-07-R.txt"

    //only use for build all flavor, if not, just ignore this field
    tinkerBuildFlavorDirectory = "${bakPath}/app-1018-17-32-47"
}


def getOldApkPath() {
    return hasProperty("OLD_APK") ? OLD_APK : ext.tinkerOldApkPath
}

def getApplyMappingPath() {
    return hasProperty("APPLY_MAPPING") ? APPLY_MAPPING : ext.tinkerApplyMappingPath
}

def getApplyResourceMappingPath() {
    return hasProperty("APPLY_RESOURCE") ? APPLY_RESOURCE : ext.tinkerApplyResourcePath
}

def getTinkerIdValue() {
    //不使用独立的tinker_id，使用版本号作为标记。
    //return hasProperty("TINKER_ID") ? TINKER_ID : gitSha()
    System.out.println("tinker id is: " + rootProject.ext.versions.versionCode)
    return rootProject.ext.versions.versionCode ? rootProject.ext.versions.versionCode : gitSha()
}

def buildWithTinker() {
    return hasProperty("TINKER_ENABLE") ? Boolean.parseBoolean(TINKER_ENABLE) : ext.tinkerEnabled
}

def getTinkerBuildFlavorDirectory() {
    return ext.tinkerBuildFlavorDirectory
}

if (buildWithTinker()) {
    apply plugin: 'com.tencent.tinker.patch'

    tinkerPatch {
        /**
         * necessary，default 'null'
         * the old apk path, use to diff with the new apk to build
         * add apk from the build/bakApk
         */
        oldApk = getOldApkPath()
        /**
         * optional，default 'false'
         * there are some cases we may get some warnings
         * if ignoreWarning is true, we would just assert the patch process
         * case 1: minSdkVersion is below 14, but you are using dexMode with raw.
         *         it must be crash when load.
         * case 2: newly added Android Component in AndroidManifest.xml,
         *         it must be crash when load.
         * case 3: loader classes in dex.loader{} are not keep in the main dex,
         *         it must be let tinker not work.
         * case 4: loader classes in dex.loader{} changes,
         *         loader classes is ues to load patch dex. it is useless to change them.
         *         it won't crash, but these changes can't effect. you may ignore it
         * case 5: resources.arsc has changed, but we don't use applyResourceMapping to build
         */
        ignoreWarning = false

        /**
         * optional，default 'true'
         * whether sign the patch file
         * if not, you must do yourself. otherwise it can't check success during the patch loading
         * we will use the sign config with your build type
         */
        useSign = true

        /**
         * optional，default 'true'
         * whether use tinker to build
         */
        tinkerEnable = buildWithTinker()

        /**
         * Warning, applyMapping will affect the normal android build!
         */
        buildConfig {
            /**
             * optional，default 'null'
             * if we use tinkerPatch to build the patch apk, you'd better to apply the old
             * apk mapping file if minifyEnabled is enable!
             * Warning:
             * you must be careful that it will affect the normal assemble build!
             */
            applyMapping = getApplyMappingPath()
            /**
             * optional，default 'null'
             * It is nice to keep the resource id from R.txt file to reduce java changes
             */
            applyResourceMapping = getApplyResourceMappingPath()

            /**
             * necessary，default 'null'
             * because we don't want to check the base apk with md5 in the runtime(it is slow)
             * tinkerId is use to identify the unique base apk when the patch is tried to apply.
             * we can use git rev, svn rev or simply versionCode.
             * we will gen the tinkerId in your manifest automatic
             */
            tinkerId = getTinkerIdValue()

            /**
             * if keepDexApply is true, class in which dex refer to the old apk.
             * open this can reduce the dex diff file size.
             */
            keepDexApply = false

            /**
             * optional, default 'false'
             * Whether tinker should treat the base apk as the one being protected by app
             * protection tools.
             * If this attribute is true, the generated patch package will contain a
             * dex including all changed classes instead of any dexdiff patch-info files.
             */
            isProtectedApp = false

            /**
             * optional, default 'false'
             * Whether tinker should support component hotplug (add new component dynamically).
             * If this attribute is true, the component added in new apk will be available after
             * patch is successfully loaded. Otherwise an error would be announced when generating patch
             * on compile-time.
             *
             * <b>Notice that currently this feature is incubating and only support NON-EXPORTED Activity</b>
             */
            supportHotplugComponent = false
        }

        dex {
            /**
             * optional，default 'jar'
             * only can be 'raw' or 'jar'. for raw, we would keep its original format
             * for jar, we would repack dexes with zip format.
             * if you want to support below 14, you must use jar
             * or you want to save rom or check quicker, you can use raw mode also
             */
            dexMode = "jar"

            /**
             * necessary，default '[]'
             * what dexes in apk are expected to deal with tinkerPatch
             * it support * or ? pattern.
             */
            pattern = ["classes*.dex",
                       "assets/secondary-dex-?.jar"]
            /**
             * necessary，default '[]'
             * Warning, it is very very important, loader classes can't change with patch.
             * thus, they will be removed from patch dexes.
             * you must put the following class into main dex.
             * Simply, you should add your own application {@code tinker.sample.android.SampleApplication}
             * own tinkerLoader, and the classes you use in them
             *
             */
            loader = [
                    //use sample, let BaseBuildInfo unchangeable with tinker
                    "tinker.sample.android.app.BaseBuildInfo"
            ]
        }

        lib {
            /**
             * optional，default '[]'
             * what library in apk are expected to deal with tinkerPatch
             * it support * or ? pattern.
             * for library in assets, we would just recover them in the patch directory
             * you can get them in TinkerLoadResult with Tinker
             */
            pattern = ["lib/*/*.so"]
        }

        res {
            /**
             * optional，default '[]'
             * what resource in apk are expected to deal with tinkerPatch
             * it support * or ? pattern.
             * you must include all your resources in apk here,
             * otherwise, they won't repack in the new apk resources.
             */
            pattern = ["res/*", "assets/*", "resources.arsc", "AndroidManifest.xml"]

            /**
             * optional，default '[]'
             * the resource file exclude patterns, ignore add, delete or modify resource change
             * it support * or ? pattern.
             * Warning, we can only use for files no relative with resources.arsc
             */
            ignoreChange = ["assets/sample_meta.txt"]

            /**
             * default 100kb
             * for modify resource, if it is larger than 'largeModSize'
             * we would like to use bsdiff algorithm to reduce patch file size
             */
            largeModSize = 100
        }

        packageConfig {
            /**
             * optional，default 'TINKER_ID, TINKER_ID_VALUE' 'NEW_TINKER_ID, NEW_TINKER_ID_VALUE'
             * package meta file gen. path is assets/package_meta.txt in patch file
             * you can use securityCheck.getPackageProperties() in your ownPackageCheck method
             * or TinkerLoadResult.getPackageConfigByName
             * we will get the TINKER_ID from the old apk manifest for you automatic,
             * other config files (such as patchMessage below)is not necessary
             */
            configField("patchMessage", "tinker is sample to use")
            /**
             * just a sample case, you can use such as sdkVersion, brand, channel...
             * you can parse it in the SamplePatchListener.
             * Then you can use patch conditional!
             */
            configField("platform", "all")
            /**
             * patch version via packageConfig
             */
            configField("patchVersion", "1.0")
        }
        //or you can add config filed outside, or get meta value from old apk
        //project.tinkerPatch.packageConfig.configField("test1", project.tinkerPatch.packageConfig.getMetaDataFromOldApk("Test"))
        //project.tinkerPatch.packageConfig.configField("test2", "sample")

        /**
         * if you don't use zipArtifact or path, we just use 7za to try
         */
        sevenZip {
            /**
             * optional，default '7za'
             * the 7zip artifact path, it will use the right 7za with your platform
             */
            zipArtifact = "com.tencent.mm:SevenZip:1.1.10"
            /**
             * optional，default '7za'
             * you can specify the 7za path yourself, it will overwrite the zipArtifact value
             */
//        path = "/usr/local/bin/7za"
        }
    }

    List<String> flavors = new ArrayList<>();
    project.android.productFlavors.each { flavor ->
        flavors.add(flavor.name)
    }
    boolean hasFlavors = flavors.size() > 0
    def date = new Date().format("MMdd-HH-mm-ss")

    /**
     * bak apk and mapping
     */
    android.applicationVariants.all { variant ->
        /**
         * task type, you want to bak
         */
        def taskName = variant.name

        tasks.all {
            if ("assemble${taskName.capitalize()}".equalsIgnoreCase(it.name)) {

                it.doLast {
                    copy {
                        def fileNamePrefix = "${project.name}-${variant.baseName}"
                        def newFileNamePrefix = hasFlavors ? "${fileNamePrefix}" : "${fileNamePrefix}-${date}"

                        def destPath = hasFlavors ? file("${bakPath}/${project.name}-${date}/${variant.flavorName}") : bakPath

                        if (variant.metaClass.hasProperty(variant, 'packageApplicationProvider')) {
                            def packageAndroidArtifact = variant.packageApplicationProvider.get()
                            if (packageAndroidArtifact != null) {
                                try {
                                    from new File(packageAndroidArtifact.outputDirectory.getAsFile().get(), variant.outputs.first().apkData.outputFileName)
                                } catch (Exception e) {
                                    from new File(packageAndroidArtifact.outputDirectory, variant.outputs.first().apkData.outputFileName)
                                }
                            } else {
                                from variant.outputs.first().mainOutputFile.outputFile
                            }
                        } else {
                            from variant.outputs.first().outputFile
                        }

                        into destPath
                        rename { String fileName ->
                            fileName.replace("${fileNamePrefix}.apk", "${newFileNamePrefix}.apk")
                        }

                        from "${buildDir}/outputs/mapping/${variant.dirName}/mapping.txt"
                        into destPath
                        rename { String fileName ->
                            fileName.replace("mapping.txt", "${newFileNamePrefix}-mapping.txt")
                        }

                        from "${buildDir}/intermediates/symbols/${variant.dirName}/R.txt"
                        from "${buildDir}/intermediates/symbol_list/${variant.dirName}/R.txt"
                        into destPath
                        rename { String fileName ->
                            fileName.replace("R.txt", "${newFileNamePrefix}-R.txt")
                        }
                    }
                }
            }
        }
    }
    project.afterEvaluate {
        //sample use for build all flavor for one time
        if (hasFlavors) {
            task(tinkerPatchAllFlavorRelease) {
                group = 'tinker'
                def originOldPath = getTinkerBuildFlavorDirectory()
                for (String flavor : flavors) {
                    def tinkerTask = tasks.getByName("tinkerPatch${flavor.capitalize()}Release")
                    dependsOn tinkerTask
                    def preAssembleTask = tasks.getByName("process${flavor.capitalize()}ReleaseManifest")
                    preAssembleTask.doFirst {
                        String flavorName = preAssembleTask.name.substring(7, 8).toLowerCase() + preAssembleTask.name.substring(8, preAssembleTask.name.length() - 15)
                        project.tinkerPatch.oldApk = "${originOldPath}/${flavorName}/${project.name}-${flavorName}-release.apk"
                        project.tinkerPatch.buildConfig.applyMapping = "${originOldPath}/${flavorName}/${project.name}-${flavorName}-release-mapping.txt"
                        project.tinkerPatch.buildConfig.applyResourceMapping = "${originOldPath}/${flavorName}/${project.name}-${flavorName}-release-R.txt"

                    }

                }
            }

            task(tinkerPatchAllFlavorDebug) {
                group = 'tinker'
                def originOldPath = getTinkerBuildFlavorDirectory()
                for (String flavor : flavors) {
                    def tinkerTask = tasks.getByName("tinkerPatch${flavor.capitalize()}Debug")
                    dependsOn tinkerTask
                    def preAssembleTask = tasks.getByName("process${flavor.capitalize()}DebugManifest")
                    preAssembleTask.doFirst {
                        String flavorName = preAssembleTask.name.substring(7, 8).toLowerCase() + preAssembleTask.name.substring(8, preAssembleTask.name.length() - 13)
                        project.tinkerPatch.oldApk = "${originOldPath}/${flavorName}/${project.name}-${flavorName}-debug.apk"
                        project.tinkerPatch.buildConfig.applyMapping = "${originOldPath}/${flavorName}/${project.name}-${flavorName}-debug-mapping.txt"
                        project.tinkerPatch.buildConfig.applyResourceMapping = "${originOldPath}/${flavorName}/${project.name}-${flavorName}-debug-R.txt"
                    }

                }
            }
        }
    }
}



task sortPublicTxt() {
    doLast {
        File originalFile = project.file("public.txt")
        File sortedFile = project.file("public_sort.txt")
        List<String> sortedLines = new ArrayList<>()
        originalFile.eachLine {
            sortedLines.add(it)
        }
        Collections.sort(sortedLines)
        sortedFile.delete()
        sortedLines.each {
            sortedFile.append("${it}\n")
        }
    }
}
```

## 5.需要从 Tinker sample 里复制过来这些类
BuildInfo.java <br />
SampleApplicationContext.java <br />
SampleApplicationLike.java <br />
SampleLoadReporter.java <br />
SamplePatchListener.java <br />
SamplePatchReporter.java <br />
SampleResultService.java <br />
SampleTinkerReport.java <br />
SampleUncaughtExceptionHandler.java <br />      
TinkerManager.java <br />
Utils.java <br />

## 6.Application 类的修改
[Tinker 自定义扩展](https://github.com/Tencent/tinker/wiki/Tinker-自定义扩展)
#### 6.1将我们自己Application类以及它的继承类的所有代码拷贝到自己的ApplicationLike继承类中，例如SampleApplicationLike。你也可以直接将自己的Application改为继承ApplicationLike;
#### 6.2Application的attachBaseContext方法实现要单独移动到onBaseContextAttached中，其他逻辑代码也需要移动过来；
#### 6.3对ApplicationLike中，引用application的地方改成getApplication();
#### 6.4对其他引用Application或者它的静态对象与方法的地方，改成引用ApplicationLike的静态对象与方法；
#### 6.5在注解 DefaultLifeCycle 里面的值 application 写好 application 的包名+类名，build一下，修改 AndroidManifest.xml 的 application 名字为这个；

## 7.AndroidManifest.xml
#### 7.1替换运来的 Application 类
#### 7.2添加一个 tinker service 的注册
```
<application
    android:name=".tinker.SampleApplication"
    ...
    
    <service
        android:name=".tinker.SampleResultService"
        android:permission="android.permission.BIND_JOB_SERVICE"
        android:exported="false"/>
</application>
```

## 8.代码里使用热修复
#### 重点代码：
```
TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
```

其他代码：
#### 删除 patch 代码
```
Tinker.with(getApplicationContext()).cleanPatch();
```
#### 杀死当前进程
```
ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
android.os.Process.killProcess(android.os.Process.myPid());
```
#### 判断tinker有没有加载patch
```
Tinker tinker = Tinker.with(getApplicationContext());
if (tinker.isTinkerLoaded()) {
```
#### 可供使用的信息  部分需要在 build.gradle 里面的 packageConfig 闭包里声明
```
final StringBuilder sb = new StringBuilder();
Tinker tinker = Tinker.with(getApplicationContext());
if (tinker.isTinkerLoaded()) {
    sb.append(String.format("[patch is loaded] \n"));
    sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
    sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));
    sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
    sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
    sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
    sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));
} else {
    sb.append(String.format("[patch is not loaded] \n"));
    sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
    sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));
    sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
    sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
}
sb.append(String.format("[BaseBuildInfo Message] %s \n", BaseBuildInfo.TEST_MESSAGE));
```



<br /><br /><br />
——————————————————————————————
# 注意事项
#### 1.注意要关闭Instant run
#### 2.tinkerApplyResourcePath 也要改变
#### 3.加载成功后会删除修复包

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