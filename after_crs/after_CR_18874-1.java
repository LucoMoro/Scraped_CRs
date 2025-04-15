/*ADT using the new layoutlib API.

Change-Id:I8ab6cbd4e1d32348fb33ee5759420a23c6d2c433*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java
//Synthetic comment -- index 505e2c0..103525c 100644

//Synthetic comment -- @@ -74,7 +74,9 @@
boolean dispose();

/**
     * Starts a layout session by inflating and rendering it. The method returns a
     * {@link ILayoutScene} on which further actions can be taken.
     *
* @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
* layout file.
* @param projectKey An Object identifying the project. This is used for the cache mechanism.







