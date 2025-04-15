/*code cleanup: unused import statements, local varaiables and ... removed from Camera app

Change-Id:I6480b49bffa898400b2f2f3eeea85ce3bceed457*/
//Synthetic comment -- diff --git a/src/com/android/camera/Camera.java b/src/com/android/camera/Camera.java
//Synthetic comment -- index f0d38af..f6b653e 100644

//Synthetic comment -- @@ -42,12 +42,11 @@
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
//Synthetic comment -- @@ -370,7 +369,7 @@
}

private void addIdleHandler() {
        MessageQueue queue = getMainLooper().myQueue();
queue.addIdleHandler(new MessageQueue.IdleHandler() {
public boolean queueIdle() {
ImageManager.ensureOSXCompatibleFolder();
//Synthetic comment -- @@ -1005,14 +1004,14 @@
CameraSettings.KEY_WHITE_BALANCE, whiteBalance);
mHeadUpDisplay.overrideSettings(
CameraSettings.KEY_FOCUS_MODE, focusMode);
            }});        
}

    private void updateSceneModeInHud() {        
// If scene mode is set, we cannot set flash mode, white balance, and
        // focus mode, instead, we read it from driver        
if (!Parameters.SCENE_MODE_AUTO.equals(mSceneMode)) {
            overrideHudSettings(mParameters.getFlashMode(), 
mParameters.getWhiteBalance(), mParameters.getFocusMode());
} else {
overrideHudSettings(null, null, null);
//Synthetic comment -- @@ -1664,9 +1663,6 @@
setPreviewDisplay(mSurfaceHolder);
setCameraParameters(UPDATE_PARAM_ALL);

        final long wallTimeStart = SystemClock.elapsedRealtime();
        final long threadTimeStart = Debug.threadCpuTimeNanos();

mCameraDevice.setErrorCallback(mErrorCallback);

try {








//Synthetic comment -- diff --git a/src/com/android/camera/CameraSettings.java b/src/com/android/camera/CameraSettings.java
//Synthetic comment -- index 5abe73c..be30140 100644

//Synthetic comment -- @@ -69,7 +69,6 @@
// MMS video length
public static final int DEFAULT_VIDEO_DURATION_VALUE = -1;

    @SuppressWarnings("unused")
private static final String TAG = "CameraSettings";

private final Context mContext;
//Synthetic comment -- @@ -237,8 +236,6 @@
private void filterUnsupportedOptions(PreferenceGroup group,
ListPreference pref, List<String> supported) {

        CharSequence[] allEntries = pref.getEntries();

// Remove the preference if the parameter is not supported or there is
// only one options for the settings.
if (supported == null || supported.size() <= 1) {








//Synthetic comment -- diff --git a/src/com/android/camera/Switcher.java b/src/com/android/camera/Switcher.java
//Synthetic comment -- index c88ddb8..1fa9caa 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;








//Synthetic comment -- diff --git a/src/com/android/camera/ThumbnailController.java b/src/com/android/camera/ThumbnailController.java
//Synthetic comment -- index 6b67cb0..8bc5dd9 100644

//Synthetic comment -- @@ -46,7 +46,6 @@
*/
public class ThumbnailController {

    @SuppressWarnings("unused")
private static final String TAG = "ThumbnailController";
private final ContentResolver mContentResolver;
private Uri mUri;








//Synthetic comment -- diff --git a/src/com/android/camera/gallery/Image.java b/src/com/android/camera/gallery/Image.java
//Synthetic comment -- index 0726c21..495f65d 100644

//Synthetic comment -- @@ -23,8 +23,6 @@
* The class for normal images in gallery.
*/
public class Image extends BaseImage implements IImage {
    private static final String TAG = "BaseImage";

private final int mRotation;

public Image(ContentResolver cr,








//Synthetic comment -- diff --git a/src/com/android/camera/ui/GLRootView.java b/src/com/android/camera/ui/GLRootView.java
//Synthetic comment -- index d8ae0f8..8aeae04 100644

//Synthetic comment -- @@ -92,9 +92,6 @@

private boolean mIsQueueActive = true;

    private int mFirstWidth;
    private int mFirstHeight;

// TODO: move this part (handler) into GLSurfaceView
private final Looper mLooper;








