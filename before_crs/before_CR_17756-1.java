/*Fixing Issue 2021:   ADT Layout Editor can't handle TabWidget

Change-Id:Ib9dba540caf337a9040ed84a164f1cde00bcbe1e*/
//Synthetic comment -- diff --git a/core/java/android/widget/TabHost.java b/core/java/android/widget/TabHost.java
//Synthetic comment -- index 02cd6a8..9e291ba 100644

//Synthetic comment -- @@ -515,7 +515,7 @@
if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
// Donut apps get old color scheme
tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
                tv.setTextColor(context.getResources().getColorStateList(R.color.tab_indicator_text_v4));
}

return tabIndicator;








//Synthetic comment -- diff --git a/tools/layoutlib/bridge/src/com/android/layoutlib/bridge/Bridge.java b/tools/layoutlib/bridge/src/com/android/layoutlib/bridge/Bridge.java
//Synthetic comment -- index 4201e80..8ba7bf2 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.tools.layoutlib.create.MethodAdapter;
import com.android.tools.layoutlib.create.OverrideMethod;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
//Synthetic comment -- @@ -77,6 +78,7 @@

private static final int DEFAULT_TITLE_BAR_HEIGHT = 25;
private static final int DEFAULT_STATUS_BAR_HEIGHT = 25;

public static class StaticMethodNotImplementedException extends RuntimeException {
private static final long serialVersionUID = 1L;
//Synthetic comment -- @@ -150,7 +152,7 @@
* @see com.android.layoutlib.api.ILayoutBridge#getApiLevel()
*/
public int getApiLevel() {
        return API_CURRENT;
}

/*
//Synthetic comment -- @@ -369,7 +371,8 @@
metrics.xdpi = xdpi;
metrics.ydpi = ydpi;

            context = new BridgeContext(projectKey, metrics, currentTheme, projectResources,
frameworkResources, styleParentMap, customViewLoader, logger);
BridgeInflater inflater = new BridgeInflater(context, customViewLoader);
context.setBridgeInflater(inflater);
//Synthetic comment -- @@ -1167,4 +1170,27 @@
}
}

}








//Synthetic comment -- diff --git a/tools/layoutlib/bridge/src/com/android/layoutlib/bridge/BridgeContext.java b/tools/layoutlib/bridge/src/com/android/layoutlib/bridge/BridgeContext.java
//Synthetic comment -- index 744bfbe6..22d1e99 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
//Synthetic comment -- @@ -87,6 +88,7 @@
private final IProjectCallback mProjectCallback;
private final ILayoutLog mLogger;
private BridgeContentResolver mContentResolver;

/**
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
//Synthetic comment -- @@ -109,7 +111,15 @@
Map<String, Map<String, IResourceValue>> frameworkResources,
Map<IStyleResourceValue, IStyleResourceValue> styleInheritanceMap,
IProjectCallback customViewLoader, ILayoutLog logger) {
        mProjectKey = projectKey;
mProjectCallback = customViewLoader;
mLogger = logger;
Configuration config = new Configuration();
//Synthetic comment -- @@ -991,8 +1001,7 @@

@Override
public ApplicationInfo getApplicationInfo() {
        // TODO Auto-generated method stub
        return null;
}

@Override







