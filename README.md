Android-Gradle-Multy-Flavors
===
Blog Link：http://www.septenary.cn/article/75

试想一下我们用 **Gradle** 构建多个渠道包，而且这些渠道包都是有差异的，可能是包名不同，可能是代码不同，也可能是资源不同，**flavors**  这个概念由此产生，用  **flavors** 来配置构建脚本 **build.gradle** ，可以构建 Android 工程的多个变种

---

最简单的 flavors 配置
---
在 **build.gradle** 文件中，构建多个变种，最简单的配置如下

    productFlavors {
        googlePlay {
        }
        appStore {
        }
    }

执行 `gradle build` 后你会看见，**./output/apk 文件夹** 下生成了 **googlePlay** 和 **appStore** 两种 APK 文件，接下来就以这两个变种为例，详细介绍下如何配置 **flavors**

多个包名
---
下面配置的 `applicationId` 会在打包时替换 **AndroidManifest.xml** 中的 `package="xx.xx"`

    productFlavors {
        googlePlay {
            applicationId 'cn.septenary.mulityflavors_googlePlay'
        }
        appStore {
            applicationId 'cn.septenary.mulityflavors_appStore'
        }
    }

多个BuildConfig
---
**BuildConfig** 是 Android 构建的时候自动生成配置文件，最常用的就是常量 `BuildConfig.DEBUG` ，便于开发者区分不同的版本来编写不同的代码，下面代码介绍了在 **BuildConfig** 中根据不同的 **flavor**  配置不同的自定义常量

    productFlavors {
        googlePlay {
            buildConfigField "String","SotreName","\"Google 应用商店\""
        }
        appStore {
            buildConfigField "String","SotreName","\"苹果 应用商店\""
        }
    }

其中，`buildConfigField` 需要三个变量，第一个参数定义了**常量类型**，第二个参数为**常量名**，第三个参数定义了**常量值**，这里需要注意第三个参数，由于定义的常量是 String 类型，所以用到了 `\"`进行转义，在代码中访问 `BuildConfig.SotreName`  就可以拿到不同 **flavor** 对应的 `SotreName` 的值了。


同时修改多个 flavor
---
如果你的项目多个flavor用到了同一个配置，可以这样写
    
    productFlavors.all { flavor ->
        // replace all buildConfigField -> SotreName
        flavor.buildConfigField 'String', 'SotreName', '"默认商店名"'
    }


多资源
---
如果一个应用，在不同的渠道需要不同的启动图或者文字提示，flavor 同样可以帮我们完成。

1.首先，Android Studio 的工程目录默认地将源代码和资源都放在了 **src/main** 文件夹下，在 **main** 的同级目录，也就是  **src** 文件夹下创建全路径文件 **src/googlePlay/res/values/strings.xml**，（注意：路径中的 "googlePlay" 必须与 flavor 名称一一对应），这个文件代码如下：
        
    <resources>
        <string name="app_name">Flavor: Google Play</string>
    </resources>
    
2.同样地，创建全路径文件 **src/appStore/res/values/strings.xml**，配置下面的代码:

    <resources>
        <string name="app_name">Flavor: App Store</string>
    </resources>
    
3.在java源代码中调用 `getResources().getString(R.string.hello_flavor)` ，不同的 flavor 构建成的 APK 运行起来打印出的结果便不同了

4.如果想要替换其他资源，同样地按照 **src + flavor名称 + 对应要修改的文件** 这种方式，都可以完成，这种方式同样适用于替换源代码（类名，包名必须保持一致）


占位符 Placeholder
---
如果你想在代码中从 **AndroidManifest.xml **，通过不同的 flavor 读取不同的 **meta-data** 值，该如何实现呢？

**Placeholder** 占位符 这个概念，可以帮我们完成，下面是如何使用占位符的示例：

1. 在 **AndroidManifest** 文件中配置：
    
    &#60;application&#62;
        <meta-data
                android:name="CHANNEL"
                android:value="${CHANNEL_VALUE}"/>
            。。。
    &#60;&#47;application&#62;

其中 `${CHANNEL_VALUE}` 为占位符，下面通过这个这个占位符对不同的 flavor 进行配置

2. 在 **build.config** 中配置占位符：
    
        defaultConfig {
            // Placeholder
            manifestPlaceholders = [CHANNEL_VALUE: 'channel_testing']
        }
        
        productFlavors.all { flavor ->
            // replace all placeholders
            flavor.manifestPlaceholders.put("CHANNEL_VALUE", name)
        }
        
        productFlavors {
            googlePaly {
                ...
            }
            appStore {
                ...
                manifestPlaceholders.put("CHANNEL_VALUE", 'channel_appstore')
            }
        }

---
指定 APK 输出
---
默认地，`gradle build` 构建出来的 APK 是构建系统默认设置的名字，如果我们想要 APK 的名字追加生成时间、指定生成路径，改怎么办呢？

1. 定义函数 buildTime
        
        def buildTime() {
            def date = new Date()
            def formattedDate = date.format('yyyy-MM-dd')
            return formattedDate
        }
        
这段代码要放在与 **android {...}** 同级下，而不是嵌入在 **android{...}** 中

2. 全局配置

        applicationVariants.all { variant ->
            variant.outputs.each { output ->
                def parent = output.outputFile.parent;
                def apkName = "${variant.flavorName}_${variant.versionName}_${buildTime()}.apk"
                output.outputFile = new File(parent, apkName);
            }
        }

3. 针对与某个 **buildType** 配置
    
        buildTypes {
            release {
                ...
            }
            debug {
                applicationVariants.all { variant ->
                    variant.outputs.each { output ->
                        if (output.outputFile != null 
                        && output.outputFile.name.endsWith('.apk') 
                        &&'debug'.equals(variant.buildType.name){
                            def parent = output.outputFile.getParent();
                            def apkName = "${variant.flavorName}_${variant.versionName}_${buildTime()}.apk"
                            def apkFile = new File(parent,apkName)
                            output.outputFile = apkFile
                        }
                    }
                }
            }
        }
    
    


Mulity Source Codes
---

**Flavor googlePaly**

1. Manually create Java file  `googlePaly/java/cn/septenary/mulityflavors/Flavor.java` in **src**
2. Custom code:

		package cn.septenary.mulityflavors;

		public class Flavor {
   			public String getFlavorName () {
        		return "Hello Code : GooglePlay";
    		}
		}


**Flavor appStore**

1. Manually create Java file  `appStore/java/cn/septenary/mulityflavors/Flavor.java` in **src**
2. Custom code:

		package cn.septenary.mulityflavors;

		public class Flavor {
   			public String getFlavorName () {
        		return "Hello Code : appStore";
    		}
		}
		
Must ensure the same **package name** & **class name**, 

then you can use the code **Flavor.java** in **MainActiviy** `new Flavor().getFlavorName()`



Finally,in termial , run 
	
	ᐅ ./gradlew assembleDebug

Install the generated debug apks, you will see :


 ![image](https://raw.githubusercontent.com/Ryfthink/Android-Gradle-Mulity-Flavor/master/art/a.png)
 ![image](https://raw.githubusercontent.com/Ryfthink/Android-Gradle-Mulity-Flavor/master/art/b.png)
	
