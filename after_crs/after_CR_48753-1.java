/*[Browser]UI control button first flash chaotically at wrong place when playing http live clip

Rotating the device orientaion when playing a video in fullscreen mode,
the controller bar flash chaotically at wrong place for a second,
and then back to right place, Now add  a interface to hide it,
since ensure it will get repositioned and reshow at a later time.

Change-Id:Iea98399b43daae51d2eeb18835782e10c045549aAuthor: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 38905*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoFullScreen.java b/core/java/android/webkit/HTML5VideoFullScreen.java
//Synthetic comment -- index 9b93805..9946f27 100644

//Synthetic comment -- @@ -316,6 +316,14 @@
return true;
}

    public void setMediaControllerHidden() {
         if (mPlayer != null && mMediaController != null
                  && mCurrentState == STATE_PREPARED
                  && mMediaController.isShowing()) {
             mMediaController.hide();
         }
    }

// MediaController FUNCTIONS:
@Override
public boolean canPause() {








//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoView.java b/core/java/android/webkit/HTML5VideoView.java
//Synthetic comment -- index 0e8a5db..89d971c 100644

//Synthetic comment -- @@ -331,6 +331,9 @@
return false;
}

    public void setMediaControllerHidden() {
    }

public void decideDisplayMode() {
}









//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index a3d62ae..4f4f46e 100644

//Synthetic comment -- @@ -185,6 +185,12 @@
}
}

        public static void setMediaControllerHidden() {
            if (mHTML5VideoView != null) {
                mHTML5VideoView.setMediaControllerHidden();
            }
        }

// This is on the UI thread.
// When native tell Java to play, we need to check whether or not it is
// still the same video by using videoLayerId and treat it differently.
//Synthetic comment -- @@ -770,6 +776,11 @@
VideoPlayer.exitFullScreenVideo(this, mWebView);
}

    public void setMediaControllerHidden() {
        VideoPlayer.setMediaControllerHidden();
    }


/**
* The factory for HTML5VideoViewProxy instances.
* @param webViewCore is the WebViewCore that is requesting the proxy.








//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index ae56e6b..a86a472 100644

//Synthetic comment -- @@ -4467,6 +4467,10 @@
if (mWebViewCore != null && !mBlockWebkitViewMessages) {
mWebViewCore.sendMessage(EventHub.CLEAR_CONTENT);
}

        if (mHTML5VideoViewProxy != null) {
            mHTML5VideoViewProxy.setMediaControllerHidden();
        }
}

/**







