/*Fixing Issue 2021: ADT Layout Editor can't handle TabWidget

	modified:   tools/layoutlib/bridge/src/com/android/layoutlib/bridge/BridgeContext.java

Change-Id:I1a9106320a0b0b30e7d40fec3a2b4393acb21036*/
//Synthetic comment -- diff --git a/tools/layoutlib/bridge/src/com/android/layoutlib/bridge/BridgeContext.java b/tools/layoutlib/bridge/src/com/android/layoutlib/bridge/BridgeContext.java
//Synthetic comment -- index 744bfbe6..8a26148 100644

//Synthetic comment -- @@ -87,6 +87,7 @@
private final IProjectCallback mProjectCallback;
private final ILayoutLog mLogger;
private BridgeContentResolver mContentResolver;

/**
* @param projectKey An Object identifying the project. This is used for the cache mechanism.
//Synthetic comment -- @@ -110,6 +111,10 @@
Map<IStyleResourceValue, IStyleResourceValue> styleInheritanceMap,
IProjectCallback customViewLoader, ILayoutLog logger) {
mProjectKey = projectKey;
mProjectCallback = customViewLoader;
mLogger = logger;
Configuration config = new Configuration();
//Synthetic comment -- @@ -991,8 +996,7 @@

@Override
public ApplicationInfo getApplicationInfo() {
        // TODO Auto-generated method stub
        return null;
}

@Override







