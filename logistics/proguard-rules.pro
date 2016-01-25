# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#不混淆
#-dontobfuscate
#不优化
#-dontoptimize

#解决泛型问题
-keepattributes Signature
-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** {*;}
-keep class com.j256.ormlite.**{*;}
-dontwarn org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.v1.XmlSerializer
-dontwarn org.codehaus.**
-dontwarn java.nio.**
-keep public class org.codehaus.** {*;}
-keep public class java.nio.** {*;}

-keep public class * implements java.io.Serializable {
   public *;
}