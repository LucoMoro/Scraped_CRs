/*Replaced internal getter calls with private variable

Regarding to:http://developer.android.com/guide/practices/design/performance.html#internal_get_setChange-Id:I36b34f7dae6eabfec288f827eca30d7371630276*/
//Synthetic comment -- diff --git a/core/java/android/app/Activity.java b/core/java/android/app/Activity.java
//Synthetic comment -- index 49ebce3..431f396 100644

//Synthetic comment -- @@ -981,8 +981,7 @@
* @see #onResume
*/
protected void onPostResume() {
        final Window win = getWindow();
        if (win != null) win.makeActive();
mCalled = true;
}

//Synthetic comment -- @@ -1609,7 +1608,7 @@
* @return The view if found or null otherwise.
*/
public View findViewById(int id) {
        return getWindow().findViewById(id);
}

/**
//Synthetic comment -- @@ -1619,7 +1618,7 @@
* @param layoutResID Resource ID to be inflated.
*/
public void setContentView(int layoutResID) {
        getWindow().setContentView(layoutResID);
}

/**
//Synthetic comment -- @@ -1630,7 +1629,7 @@
* @param view The desired content to display.
*/
public void setContentView(View view) {
        getWindow().setContentView(view);
}

/**
//Synthetic comment -- @@ -1642,7 +1641,7 @@
* @param params Layout parameters for the view.
*/
public void setContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().setContentView(view, params);
}

/**
//Synthetic comment -- @@ -1653,7 +1652,7 @@
* @param params Layout parameters for the view.
*/
public void addContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().addContentView(view, params);
}

/**
//Synthetic comment -- @@ -1786,7 +1785,7 @@
if (mDefaultKeyMode == DEFAULT_KEYS_DISABLE) {
return false;
} else if (mDefaultKeyMode == DEFAULT_KEYS_SHORTCUT) {
            if (getWindow().performPanelShortcut(Window.FEATURE_OPTIONS_PANEL, 
keyCode, event, Menu.FLAG_ALWAYS_PERFORM_CLOSE)) {
return true;
}
//Synthetic comment -- @@ -2013,9 +2012,8 @@
* @see #onWindowAttributesChanged(android.view.WindowManager.LayoutParams)
*/
public boolean hasWindowFocus() {
        Window w = getWindow();
        if (w != null) {
            View d = w.getDecorView();
if (d != null) {
return d.hasWindowFocus();
}
//Synthetic comment -- @@ -2034,12 +2032,11 @@
*/
public boolean dispatchKeyEvent(KeyEvent event) {
onUserInteraction();
        Window win = getWindow();
        if (win.superDispatchKeyEvent(event)) {
return true;
}
View decor = mDecor;
        if (decor == null) decor = win.getDecorView();
return event.dispatch(this, decor != null
? decor.getKeyDispatcherState() : null, this);
}
//Synthetic comment -- @@ -2058,7 +2055,7 @@
if (ev.getAction() == MotionEvent.ACTION_DOWN) {
onUserInteraction();
}
        if (getWindow().superDispatchTouchEvent(ev)) {
return true;
}
return onTouchEvent(ev);
//Synthetic comment -- @@ -2076,7 +2073,7 @@
*/
public boolean dispatchTrackballEvent(MotionEvent ev) {
onUserInteraction();
        if (getWindow().superDispatchTrackballEvent(ev)) {
return true;
}
return onTrackballEvent(ev);
//Synthetic comment -- @@ -2086,7 +2083,7 @@
event.setClassName(getClass().getName());
event.setPackageName(getPackageName());

        LayoutParams params = getWindow().getAttributes();
boolean isFullScreen = (params.width == LayoutParams.FILL_PARENT) &&
(params.height == LayoutParams.FILL_PARENT);
event.setFullScreen(isFullScreen);
//Synthetic comment -- @@ -2633,7 +2630,7 @@
* @see android.view.Window#takeKeyEvents
*/
public void takeKeyEvents(boolean get) {
        getWindow().takeKeyEvents(get);
}

/**
//Synthetic comment -- @@ -2648,7 +2645,7 @@
* @see android.view.Window#requestFeature
*/
public final boolean requestWindowFeature(int featureId) {
        return getWindow().requestFeature(featureId);
}

/**
//Synthetic comment -- @@ -2656,7 +2653,7 @@
* {@link android.view.Window#setFeatureDrawableResource}.
*/
public final void setFeatureDrawableResource(int featureId, int resId) {
        getWindow().setFeatureDrawableResource(featureId, resId);
}

/**
//Synthetic comment -- @@ -2664,7 +2661,7 @@
* {@link android.view.Window#setFeatureDrawableUri}.
*/
public final void setFeatureDrawableUri(int featureId, Uri uri) {
        getWindow().setFeatureDrawableUri(featureId, uri);
}

/**
//Synthetic comment -- @@ -2672,7 +2669,7 @@
* {@link android.view.Window#setFeatureDrawable(int, Drawable)}.
*/
public final void setFeatureDrawable(int featureId, Drawable drawable) {
        getWindow().setFeatureDrawable(featureId, drawable);
}

/**
//Synthetic comment -- @@ -2680,7 +2677,7 @@
* {@link android.view.Window#setFeatureDrawableAlpha}.
*/
public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        getWindow().setFeatureDrawableAlpha(featureId, alpha);
}

/**
//Synthetic comment -- @@ -2688,7 +2685,7 @@
* {@link android.view.Window#getLayoutInflater}.
*/
public LayoutInflater getLayoutInflater() {
        return getWindow().getLayoutInflater();
}

/**
//Synthetic comment -- @@ -3134,7 +3131,7 @@
void makeVisible() {
if (!mWindowAdded) {
ViewManager wm = getWindowManager();
            wm.addView(mDecor, getWindow().getAttributes());
mWindowAdded = true;
}
mDecor.setVisibility(View.VISIBLE);
//Synthetic comment -- @@ -3518,11 +3515,10 @@

protected void onTitleChanged(CharSequence title, int color) {
if (mTitleReady) {
            final Window win = getWindow();
            if (win != null) {
                win.setTitle(title);
if (color != 0) {
                    win.setTitleColor(color);
}
}
}
//Synthetic comment -- @@ -3540,7 +3536,7 @@
* @param visible Whether to show the progress bars in the title.
*/
public final void setProgressBarVisibility(boolean visible) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, visible ? Window.PROGRESS_VISIBILITY_ON :
Window.PROGRESS_VISIBILITY_OFF);
}

//Synthetic comment -- @@ -3553,7 +3549,7 @@
* @param visible Whether to show the progress bars in the title.
*/
public final void setProgressBarIndeterminateVisibility(boolean visible) {
        getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
visible ? Window.PROGRESS_VISIBILITY_ON : Window.PROGRESS_VISIBILITY_OFF);
}

//Synthetic comment -- @@ -3567,7 +3563,7 @@
* @param indeterminate Whether the horizontal progress bar should be indeterminate.
*/
public final void setProgressBarIndeterminate(boolean indeterminate) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
indeterminate ? Window.PROGRESS_INDETERMINATE_ON : Window.PROGRESS_INDETERMINATE_OFF);
}

//Synthetic comment -- @@ -3582,7 +3578,7 @@
*            bar will be completely filled and will fade out.
*/
public final void setProgress(int progress) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, progress + Window.PROGRESS_START);
}

/**
//Synthetic comment -- @@ -3599,7 +3595,7 @@
*            0 to 10000 (both inclusive).
*/
public final void setSecondaryProgress(int secondaryProgress) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
secondaryProgress + Window.PROGRESS_SECONDARY_START);
}

//Synthetic comment -- @@ -3620,7 +3616,7 @@
*        {@link AudioManager#USE_DEFAULT_STREAM_TYPE}.
*/
public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
}

/**
//Synthetic comment -- @@ -3632,7 +3628,7 @@
* @see #setVolumeControlStream(int)
*/
public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
}

/**







