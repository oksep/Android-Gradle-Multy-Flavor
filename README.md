Android-Gradle-Mulity-Flavor
===
Using Gradle to build an Android Project with mulity flavors

***


## Mulity BuildConfigs

###1.Modify every single flavor:

Add below code in build.gradle
	
	productFlavors {
   	 	googlePaly {
        	applicationId 'cn.septenary.mulityflavors_googlePlay'
        	buildConfigField "String","SotreName","\"Google 应用商店\""
    	}
    	appStore {
        	applicationId 'cn.septenary.mulityflavors_appStore'
        	buildConfigField "String","SotreName","\"苹果 应用商店\""
    	}
    }

It will generate two **BuildConfig**, call `BuildConfig.SotreName` in Java to access the filed.

###2.Modify all flavors:
	
	productFlavors.all { flavor ->
        // replace all buildConfigField -> SotreName
        flavor.buildConfigField 'String', 'SotreName', '"默认商店名"'
    }

***

## Mulity Resources

### Flavor googlePaly :

1. Manually Create resource file  `googlePaly/res/values/strings.xml` in **src**
2. Modify the fields you want to cover

	<resources>

    	<string name="app_name">Flavor: Google Paly</string>
    	<string name="hello_flavor">Hello Resource : GooglePlay !</string>

	</resources>

### Flavor appStore :

1. Manually Create resource file  `appStore/res/values/strings.xml` in **src**
2. Modify the fields you want to cover

	<resources>

    	<string name="app_name">Flavor: Google Paly</string>
    	<string name="hello_flavor">Hello Resource : GooglePlay !</string>

	</resources>
	
After that,try `getResources().getString(R.string.hello_flavor)` to see the effect.


***

## Placeholders
If you want to modify AndroidManifest.xml dynamically，just like this

###1.Use Placeholder to replace the value which you want to override.
	
	<application .../>
        <!-- Placeholder CHANNEL_VALUE -->
        <meta-data
            android:name="CHANNEL"
            android:value="${CHANNEL_VALUE}"/>
    ...
    </application>

###2.Config the CHANNEL_VALUE in build.config
	
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
***

##Specify OutPut->APKs

Using **ApplicationVariants** to take effect.

### define the function(out of `android {}`)

	def buildTime() {
    	def date = new Date()
    	def formattedDate = date.format('yyyy-MM-dd')
    	return formattedDate
	}
	
### config the buildTypes
	
	buildTypes {
        release {
            ...
        }

        debug {
            // specify the output *.apk , just modify applicationVariants
            applicationVariants.all { variant ->
                variant.outputs.each { output ->
                    if (output.outputFile != null && output.outputFile.name.endsWith('.apk') && 'debug'.equals(variant.buildType.name)) {
                        def apkFile = new File(output.outputFile.getParent(), "Mulityflavors_${variant.flavorName}_v${variant.versionName}_${buildTime()}.apk")
                        output.outputFile = apkFile
                    }
                }
            }
        }
    }


***


##Mulity Codes

###Flavor googlePaly :

1. Manually create Java file  `googlePaly/java/cn/septenary/mulityflavors/Flavor.java` in **src**
2. Custom code:

		package cn.septenary.mulityflavors;

		public class Flavor {
   			public String getFlavorName () {
        		return "Hello Code : GooglePlay";
    		}
		}


###Flavor appStore :

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
	