/*AttributeCache check NullPointerException in setAppStartingWindow "WindowManagerService.java"

Change-Id:I0501e5bea8481bf3f949d3c91e84ca64ce55c066*/
//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
//Synthetic comment -- index c833919..1d88d5b 100755

//Synthetic comment -- @@ -3795,6 +3795,12 @@
if (theme != 0) {
AttributeCache.Entry ent = AttributeCache.instance().get(pkg, theme,
com.android.internal.R.styleable.Window);
if (ent.array.getBoolean(
com.android.internal.R.styleable.Window_windowIsTranslucent, false)) {
return;







