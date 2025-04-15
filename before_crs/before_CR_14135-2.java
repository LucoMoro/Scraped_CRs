/*Start app with several activities with intent filter [Main, Launcher] was broken

When an application have more than one activities with intent filter
[Main, Launcher], then the intent with ResolverActivity wasn't created correct.

Change-Id:I2617122e07c35284862d2e0643888966ec0f7221*/
//Synthetic comment -- diff --git a/core/java/android/app/ApplicationContext.java b/core/java/android/app/ApplicationContext.java
//Synthetic comment -- index f48f150..2824cac 100644

//Synthetic comment -- @@ -1514,8 +1514,9 @@
if (resolveInfo == null) {
return null;
}
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName(packageName, resolveInfo.activityInfo.name);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
return intent;
}







