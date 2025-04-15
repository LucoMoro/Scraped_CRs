/*Fix javadoc for layout lib api.

Change-Id:I42b835ef2dc34af069567555ce9a4e4ecfcd6639*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/Capability.java b/layoutlib_api/src/com/android/layoutlib/api/Capability.java
//Synthetic comment -- index 586642f..87ceca1 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.layoutlib.api;

/**
* Enum describing the layout bridge capabilities.
*
//Synthetic comment -- @@ -36,7 +34,7 @@
EMBEDDED_LAYOUT,
/** Ability to call<br>
* {@link LayoutScene#insertChild(Object, IXmlPullParser, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
* {@link LayoutScene#removeChild(Object, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
* {@link LayoutScene#setProperty(Object, String, String)}
* */
//Synthetic comment -- @@ -44,6 +42,6 @@
/** Ability to call<br>
* {@link LayoutScene#animate(Object, String, boolean, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
* <p>If the bridge also supports {@link #VIEW_MANIPULATION} then those methods can use
     * an {@link com.android.layoutlib.api.LayoutScene.IAnimationListener}, otherwise they won't. */
ANIMATE;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java
//Synthetic comment -- index 70d099d..f5d3660 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
* <li>new render method: {@link #computeLayout(IXmlPullParser, Object, int, int, String, boolean, Map, Map, IProjectCallback, ILayoutLog)}</li>
* <li>deprecated {@link #computeLayout(IXmlPullParser, Object, int, int, String, Map, Map, IProjectCallback, ILayoutLog)}</li>
* </ul>
 * @deprecated Extend {@link LayoutBridge} instead.
*/
@Deprecated
public interface ILayoutBridge {
//Synthetic comment -- @@ -76,7 +76,7 @@

/**
* Starts a layout session by inflating and rendering it. The method returns a
     * {@link ILayoutResult} on which further actions can be taken.
*
* @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
* layout file.
//Synthetic comment -- @@ -102,7 +102,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link LayoutBridge#createScene(SceneParams)}
* @since 4
*/
@Deprecated
//Synthetic comment -- @@ -139,7 +139,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link LayoutBridge#createScene(SceneParams)}
* @since 3
*/
@Deprecated
//Synthetic comment -- @@ -172,7 +172,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link LayoutBridge#createScene(SceneParams)}
* @since 2
*/
@Deprecated
//Synthetic comment -- @@ -204,7 +204,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link LayoutBridge#createScene(SceneParams)}
* @since 1
*/
@Deprecated








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java
//Synthetic comment -- index ada71a7..a4d6da0 100644

//Synthetic comment -- @@ -19,11 +19,10 @@
import java.awt.image.BufferedImage;

/**
 * The result of a layout computation through {@link ILayoutBridge}.
*
* @since 1
 * @deprecated use {@link LayoutScene} as returned by {@link LayoutBridge#createScene(SceneParams)}
*/
public interface ILayoutResult {
/**
//Synthetic comment -- @@ -33,7 +32,7 @@

/**
* Error return code, in which case an error message is guaranteed to be defined.
     * @see #getErrorMessage()
*/
final static int ERROR = 1;









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java b/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java
//Synthetic comment -- index 6d0f2b6..c3c738f 100644

//Synthetic comment -- @@ -18,11 +18,9 @@

import org.xmlpull.v1.XmlPullParser;

/**
* Extended version of {@link XmlPullParser} to use with
 * {@link LayoutBridge#createScene(SceneParams)}
*/
public interface IXmlPullParser extends XmlPullParser {









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutBridge.java
//Synthetic comment -- index 7c66bc4..a3bd921 100644

//Synthetic comment -- @@ -66,7 +66,7 @@
* Starts a layout session by inflating and rendering it. The method returns a
* {@link LayoutScene} on which further actions can be taken.
*
     * @return a new {@link LayoutScene} object that contains the result of the scene creation and
* first rendering.
*/
public LayoutScene createScene(SceneParams params) {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 0883b45..8bfa7ed 100644

//Synthetic comment -- @@ -166,7 +166,7 @@
* If the listener is null, then the rendering is done synchronously.
* <p/>
* The child stays in the view hierarchy after the rendering is done. To remove it call
     * {@link #removeChild(Object, IAnimationListener)}
* <p/>
* The returned {@link SceneResult} object will contain the android.view.View object for
* the newly inflated child. It is accessible through {@link SceneResult#getData()}.
//Synthetic comment -- @@ -199,7 +199,7 @@
* If the listener is null, then the rendering is done synchronously.
* <p/>
* The child stays in the view hierarchy after the rendering is done. To remove it call
     * {@link #removeChild(Object, IAnimationListener)}
* <p/>
* The returned {@link SceneResult} object will contain the android.view.ViewGroup.LayoutParams
* object created from the <var>layoutParams</var> map if it was non <code>null</code>.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
//Synthetic comment -- index 1417f3d..2954671 100644

//Synthetic comment -- @@ -20,10 +20,10 @@
* Scene result class. This is an immutable class.
* <p/>
* This cannot be allocated directly, instead use
 * {@link SceneStatus#createResult()},
 * {@link SceneStatus#createResult(String, Throwable)},
 * {@link SceneStatus#createResult(String)}
 * {@link SceneStatus#createResult(Object)}
*/
public class SceneResult {








