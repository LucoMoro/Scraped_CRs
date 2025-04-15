/*New layoutlib API.

This is the new API to move to a stateful layoutlib, allowing
for faster actions on an inflated layout.

Change-Id:Ice6324c056efc6e82d5760b5f4e3d40b58938368*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java
//Synthetic comment -- index 4dbcfdc..6f12d2f 100644

//Synthetic comment -- @@ -24,6 +24,11 @@
* <p/>
* <p/>{@link #getApiLevel()} gives the ability to know which methods are available.
* <p/>
* Changes in API level 4:
* <ul>
* <li>new render method: {@link #computeLayout(IXmlPullParser, Object, int, int, boolean, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}</li>
//Synthetic comment -- @@ -43,7 +48,7 @@
*/
public interface ILayoutBridge {

    final int API_CURRENT = 4;

/**
* Returns the API level of the layout library.
//Synthetic comment -- @@ -88,9 +93,48 @@
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
* @since 4
*/
ILayoutResult computeLayout(IXmlPullParser layoutDescription,
Object projectKey,
int screenWidth, int screenHeight, boolean renderFullSize,
//Synthetic comment -- @@ -124,6 +168,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
* @since 3
*/
@Deprecated
//Synthetic comment -- @@ -156,7 +201,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated Use {@link #computeLayout(IXmlPullParser, Object, int, int, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
* @since 2
*/
@Deprecated
//Synthetic comment -- @@ -188,7 +233,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated Use {@link #computeLayout(IXmlPullParser, Object, int, int, int, float, float, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}
* @since 1
*/
@Deprecated








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java
//Synthetic comment -- index 2d8a210..ada71a7 100644

//Synthetic comment -- @@ -21,6 +21,9 @@
/**
* The result of a layout computation through
* {@link ILayoutLibBridge#computeLayout(IXmlPullParser, int, int, String, java.util.Map, java.util.Map, java.util.Map, IFontLoader, ILayoutLibLog, ICustomViewLoader)}
*/
public interface ILayoutResult {
/**
//Synthetic comment -- @@ -59,6 +62,7 @@

/**
* Layout information for a specific view.
*/
public interface ILayoutViewInfo {









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutScene.java
new file mode 100644
//Synthetic comment -- index 0000000..94eca2f

//Synthetic comment -- @@ -0,0 +1,103 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutViewInfo.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutViewInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..4c864c0

//Synthetic comment -- @@ -0,0 +1,75 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java b/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java
//Synthetic comment -- index cd43c56..8cf0325 100644

//Synthetic comment -- @@ -16,21 +16,30 @@

package com.android.layoutlib.api;

import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;

import org.xmlpull.v1.XmlPullParser;

/**
 * Extended version of {@link XmlPullParser} to use with 
 * {@link ILayoutLibBridge#computeLayout(XmlPullParser, int, int, String, java.util.Map, java.util.Map, java.util.Map, com.android.layoutlib.api.ILayoutLibBridge.IFontInfo)}
*/
public interface IXmlPullParser extends XmlPullParser {
    
/**
* Returns a key for the current XML node.
* <p/>This key will be passed back in the {@link ILayoutViewInfo} objects, allowing association
* of a particular XML node with its result from the layout computation.
*/
Object getViewKey();
}








