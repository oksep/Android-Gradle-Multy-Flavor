Android-Gradle-Mulity-Flavor
===
Using Gradle to build an Android Project with mulity flavors

***

BuildConfig
---
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


Resource
---

**Flavor googlePaly :**

1. Manually Create resource file  `googlePaly/res/values/strings.xml` in **src**
2. Modify the fields you want to cover

	<resources>

    	<string name="app_name">Flavor: Google Paly</string>
    	<string name="hello_flavor">Hello Resource : GooglePlay !</string>

	</resources>

**Flavor appStore :**

1. Manually Create resource file  `appStore/res/values/strings.xml` in **src**
2. Modify the fields you want to cover

	<resources>

    	<string name="app_name">Flavor: Google Paly</string>
    	<string name="hello_flavor">Hello Resource : GooglePlay !</string>

	</resources>
	
After that,try `getResources().getString(R.string.hello_flavor)` to see the effect.


Code
---

**Flavor googlePaly :**

1. Manually create Java file  `googlePaly/java/cn/septenary/mulityflavors/Flavor.java` in **src**
2. Custom code:

		package cn.septenary.mulityflavors;

		public class Flavor {
   			public String getFlavorName () {
        		return "Hello Code : GooglePlay";
    		}
		}


**Flavor appStore :**

1. Manually create Java file  `googlePaly/java/cn/septenary/mulityflavors/Flavor.java` in **src**
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
	