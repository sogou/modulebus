# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.sogou.annotationprocessor.**

-keep class com.sogou.modulebus.** { *; }

-keep class * implements com.sogou.modulebus.routerbus.IInterceptor

-keep class * implements com.sogou.modulebus.routerbus.IRouterRegistry

-keep public class * extends com.sogou.modulebus.functionbus.IExported

-keep class com.sogou.annotation.RouterSchema {
    *;
}

-keep @com.sogou.annotation.RouterSchema class * {
    *;
}