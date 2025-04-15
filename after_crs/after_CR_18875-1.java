/*Minor update to the layoutlib API.

Change-Id:I97cabd872d3c18abbbeef1919d42e30a494f7e69*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java
//Synthetic comment -- index 94eca2f..125266e 100644

//Synthetic comment -- @@ -52,7 +52,7 @@
/**
* Returns the result for the original call to {@link ILayoutBridge#startLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, java.util.Map, java.util.Map, IProjectCallback, ILayoutLog)}
*/
    ILayoutResult getResult();

/**
* Returns the {@link ILayoutViewInfo} object for the top level view.







