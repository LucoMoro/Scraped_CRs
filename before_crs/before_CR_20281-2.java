/*More fine grained layoutlib Capability for animation support.

Make the distinction between playing animation, animating
view insert/delete/move inside the same viewgroup and animating
move across layouts.

Change-Id:Ia9a6e4e53425a66a74ddd39796b04ed8c78d4a5a*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index abbab45..0de4c90 100644

//Synthetic comment -- @@ -39,9 +39,20 @@
* {@link LayoutScene#setProperty(Object, String, String)}
* */
VIEW_MANIPULATION,
    /** Ability to call<br>
* {@link LayoutScene#animate(Object, String, boolean, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
     * <p>If the bridge also supports {@link #VIEW_MANIPULATION} then those methods can use
     * an {@link com.android.layoutlib.api.LayoutScene.IAnimationListener}, otherwise they won't. */
    ANIMATE;
}







