/*Fixing Issue 2021: ADT Layout Editor can't handle TabWidget

This change depends onhttps://review.source.android.com/#change,19190The problem is resolved in the following way :
- GraphicalEditorPart.renderWithBridge computes apiLevel based on project and add it as params to Bridge (GraphicalEditorPart.java, line 1441 "LayoutScene scene = layoutLib.getBridge().createScene(params);"
It used projectKey, this parameter is unused in the BridgeContext
- BridgeContext use projectKey to create usable ApplicationInfo

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







