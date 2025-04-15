/*Minor update to the layoutlib API.*/




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








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java
//Synthetic comment -- index 94eca2f..125266e 100644

//Synthetic comment -- @@ -52,7 +52,7 @@
/**
* Returns the result for the original call to {@link ILayoutBridge#startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, java.util.Map, java.util.Map, IProjectCallback, ILayoutLog)}
*/
    ILayoutResult getResult();

/**
* Returns the {@link ILayoutViewInfo} object for the top level view.







