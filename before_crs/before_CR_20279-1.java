/*Use field instead of getter to meet guidelines

Use field directly instead of getWindow() calls to meet performance guidelines:http://developer.android.com/guide/practices/design/performance.html#internal_get_setChange-Id:Icb6d58576df8fdce5fd9839bd84fc734e71714cc*/
//Synthetic comment -- diff --git a/core/java/android/app/Activity.java b/core/java/android/app/Activity.java
//Synthetic comment -- index f25c4c3..a7e4ede 100644

//Synthetic comment -- @@ -68,7 +68,6 @@
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -996,8 +995,7 @@
* @see #onResume
*/
protected void onPostResume() {
        final Window win = getWindow();
        if (win != null) win.makeActive();
mCalled = true;
}

//Synthetic comment -- @@ -1644,7 +1642,7 @@
* @return The view if found or null otherwise.
*/
public View findViewById(int id) {
        return getWindow().findViewById(id);
}

/**
//Synthetic comment -- @@ -1654,7 +1652,7 @@
* @param layoutResID Resource ID to be inflated.
*/
public void setContentView(int layoutResID) {
        getWindow().setContentView(layoutResID);
}

/**
//Synthetic comment -- @@ -1665,7 +1663,7 @@
* @param view The desired content to display.
*/
public void setContentView(View view) {
        getWindow().setContentView(view);
}

/**
//Synthetic comment -- @@ -1677,7 +1675,7 @@
* @param params Layout parameters for the view.
*/
public void setContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().setContentView(view, params);
}

/**
//Synthetic comment -- @@ -1688,7 +1686,7 @@
* @param params Layout parameters for the view.
*/
public void addContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().addContentView(view, params);
}

/**
//Synthetic comment -- @@ -1821,7 +1819,7 @@
if (mDefaultKeyMode == DEFAULT_KEYS_DISABLE) {
return false;
} else if (mDefaultKeyMode == DEFAULT_KEYS_SHORTCUT) {
            if (getWindow().performPanelShortcut(Window.FEATURE_OPTIONS_PANEL, 
keyCode, event, Menu.FLAG_ALWAYS_PERFORM_CLOSE)) {
return true;
}
//Synthetic comment -- @@ -2048,9 +2046,8 @@
* @see #onWindowAttributesChanged(android.view.WindowManager.LayoutParams)
*/
public boolean hasWindowFocus() {
        Window w = getWindow();
        if (w != null) {
            View d = w.getDecorView();
if (d != null) {
return d.hasWindowFocus();
}
//Synthetic comment -- @@ -2069,12 +2066,11 @@
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
//Synthetic comment -- @@ -2093,7 +2089,7 @@
if (ev.getAction() == MotionEvent.ACTION_DOWN) {
onUserInteraction();
}
        if (getWindow().superDispatchTouchEvent(ev)) {
return true;
}
return onTouchEvent(ev);
//Synthetic comment -- @@ -2111,7 +2107,7 @@
*/
public boolean dispatchTrackballEvent(MotionEvent ev) {
onUserInteraction();
        if (getWindow().superDispatchTrackballEvent(ev)) {
return true;
}
return onTrackballEvent(ev);
//Synthetic comment -- @@ -2121,7 +2117,7 @@
event.setClassName(getClass().getName());
event.setPackageName(getPackageName());

        LayoutParams params = getWindow().getAttributes();
boolean isFullScreen = (params.width == LayoutParams.MATCH_PARENT) &&
(params.height == LayoutParams.MATCH_PARENT);
event.setFullScreen(isFullScreen);
//Synthetic comment -- @@ -2711,7 +2707,7 @@
* @see android.view.Window#takeKeyEvents
*/
public void takeKeyEvents(boolean get) {
        getWindow().takeKeyEvents(get);
}

/**
//Synthetic comment -- @@ -2726,7 +2722,7 @@
* @see android.view.Window#requestFeature
*/
public final boolean requestWindowFeature(int featureId) {
        return getWindow().requestFeature(featureId);
}

/**
//Synthetic comment -- @@ -2734,7 +2730,7 @@
* {@link android.view.Window#setFeatureDrawableResource}.
*/
public final void setFeatureDrawableResource(int featureId, int resId) {
        getWindow().setFeatureDrawableResource(featureId, resId);
}

/**
//Synthetic comment -- @@ -2742,7 +2738,7 @@
* {@link android.view.Window#setFeatureDrawableUri}.
*/
public final void setFeatureDrawableUri(int featureId, Uri uri) {
        getWindow().setFeatureDrawableUri(featureId, uri);
}

/**
//Synthetic comment -- @@ -2750,7 +2746,7 @@
* {@link android.view.Window#setFeatureDrawable(int, Drawable)}.
*/
public final void setFeatureDrawable(int featureId, Drawable drawable) {
        getWindow().setFeatureDrawable(featureId, drawable);
}

/**
//Synthetic comment -- @@ -2758,7 +2754,7 @@
* {@link android.view.Window#setFeatureDrawableAlpha}.
*/
public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        getWindow().setFeatureDrawableAlpha(featureId, alpha);
}

/**
//Synthetic comment -- @@ -2766,7 +2762,7 @@
* {@link android.view.Window#getLayoutInflater}.
*/
public LayoutInflater getLayoutInflater() {
        return getWindow().getLayoutInflater();
}

/**
//Synthetic comment -- @@ -3212,7 +3208,7 @@
void makeVisible() {
if (!mWindowAdded) {
ViewManager wm = getWindowManager();
            wm.addView(mDecor, getWindow().getAttributes());
mWindowAdded = true;
}
mDecor.setVisibility(View.VISIBLE);
//Synthetic comment -- @@ -3586,11 +3582,10 @@

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
//Synthetic comment -- @@ -3608,7 +3603,7 @@
* @param visible Whether to show the progress bars in the title.
*/
public final void setProgressBarVisibility(boolean visible) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, visible ? Window.PROGRESS_VISIBILITY_ON :
Window.PROGRESS_VISIBILITY_OFF);
}

//Synthetic comment -- @@ -3621,7 +3616,7 @@
* @param visible Whether to show the progress bars in the title.
*/
public final void setProgressBarIndeterminateVisibility(boolean visible) {
        getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
visible ? Window.PROGRESS_VISIBILITY_ON : Window.PROGRESS_VISIBILITY_OFF);
}

//Synthetic comment -- @@ -3635,7 +3630,7 @@
* @param indeterminate Whether the horizontal progress bar should be indeterminate.
*/
public final void setProgressBarIndeterminate(boolean indeterminate) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
indeterminate ? Window.PROGRESS_INDETERMINATE_ON : Window.PROGRESS_INDETERMINATE_OFF);
}

//Synthetic comment -- @@ -3650,7 +3645,7 @@
*            bar will be completely filled and will fade out.
*/
public final void setProgress(int progress) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, progress + Window.PROGRESS_START);
}

/**
//Synthetic comment -- @@ -3667,7 +3662,7 @@
*            0 to 10000 (both inclusive).
*/
public final void setSecondaryProgress(int secondaryProgress) {
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
secondaryProgress + Window.PROGRESS_SECONDARY_START);
}

//Synthetic comment -- @@ -3688,7 +3683,7 @@
*        {@link AudioManager#USE_DEFAULT_STREAM_TYPE}.
*/
public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
}

/**
//Synthetic comment -- @@ -3700,7 +3695,7 @@
* @see #setVolumeControlStream(int)
*/
public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
}

/**







