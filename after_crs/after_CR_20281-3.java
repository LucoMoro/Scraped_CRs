/*More fine grained layoutlib Capability for animation support.

Make the distinction between playing animation, animating
view insert/delete/move inside the same viewgroup and animating
move across layouts.

Change-Id:Ia9a6e4e53425a66a74ddd39796b04ed8c78d4a5a*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index abbab45..fc390db 100644

//Synthetic comment -- @@ -36,12 +36,26 @@
* {@link LayoutScene#insertChild(Object, IXmlPullParser, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
* {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
* {@link LayoutScene#removeChild(Object, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#setProperty(Object, String, String)}<br>
     * The method that receives an animation listener can only use it if the
     * ANIMATED_VIEW_MANIPULATION, or FULL_ANIMATED_VIEW_MANIPULATION is also supported.
     *
* */
VIEW_MANIPULATION,
    /** Ability to play animations with<br>
* {@link LayoutScene#animate(Object, String, boolean, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
     */
    PLAY_ANIMATION,
    /**
     * Ability to manipulate views with animation, as long as the view does not change parent.
     * {@link LayoutScene#insertChild(Object, IXmlPullParser, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#removeChild(Object, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     */
    ANIMATED_VIEW_MANIPULATION,
    /**
     * Ability to move views (even into a different ViewGroup) with animation.
     * see {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
     */
    FULL_ANIMATED_VIEW_MANIPULATION;
}







