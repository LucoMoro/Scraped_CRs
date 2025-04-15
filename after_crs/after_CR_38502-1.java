/*Fixed surface reconnection when effect is active

The Google Filter Framework has asynchronous API to stop the effect preview.
The stopping is delayed and disconnect from surface is done in main thread
somewhere later. There is no way to wait for the stop to be finished in
onPause(), because stop callback will be triggered in main thread as well.

In case when activity is quickly paused/resumed between triggering effect
stop and actual stop, surface will be still in connected state and attempt
to give it to Camera device will be aborted with error:

SurfaceTexture: [SurfaceView] connect: already connected (cur=1, req=4)

This patch adds waiting until effect stop will be finished on video
activity resume.

Change-Id:I02198d883235a17b7182ab0c22556ea28bcd8c53Signed-off-by: Daniel Levin <dendy@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/camera/EffectsRecorder.java b/src/com/android/camera/EffectsRecorder.java
//Synthetic comment -- index b649bc2..e033d10 100644

//Synthetic comment -- @@ -717,6 +717,10 @@
mState = STATE_PREVIEW;
}

    public boolean isStopping() {
        return mOldRunner != null;
    }

// Stop and release effect resources
public synchronized void stopPreview() {
if (mLogVerbose) Log.v(TAG, "Stopping preview (" + this + ")");
//Synthetic comment -- @@ -822,6 +826,11 @@
glEnv.deactivate();
}
mOldRunner = null;

                    final VideoCamera activity = (VideoCamera)mContext;
                    if (activity.isWaitingForEffectRecorder()) {
                        activity.processResume();
                    }
}
if (mState == STATE_PREVIEW ||
mState == STATE_STARTING_PREVIEW) {








//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index e93fbca..4fc990c 100755

//Synthetic comment -- @@ -267,6 +267,8 @@
private ZoomControl mZoomControl;
private final ZoomListener mZoomListener = new ZoomListener();

    private boolean mIsWaitingForEffectRecorder = false;

// This Handler is used to post message back onto the main thread of the
// application
private class MainHandler extends Handler {
//Synthetic comment -- @@ -819,8 +821,21 @@
(double) mProfile.videoFrameWidth / mProfile.videoFrameHeight);
}

    public boolean isWaitingForEffectRecorder() {
        return mIsWaitingForEffectRecorder;
    }

@Override
protected void doOnResume() {
        if (mEffectsRecorder != null && mEffectsRecorder.isStopping()) {
            mIsWaitingForEffectRecorder = true;
        } else {
            processResume();
        }
    }

    public void processResume() {
        mIsWaitingForEffectRecorder = false;
if (mOpenCameraFail || mCameraDisabled) return;

mPausing = false;







