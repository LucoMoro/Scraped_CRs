/*Mms: The time is not displayed on the progress bar when playing a slide

TestCase: 1> share some pictures from Gallery, preview and then
             check the media controller.
          2> receive a slideshow mms, play it and then check the
             media controller
The time is not displayed on the progress bar

We need to Use Theme_DeviceDefault to set the media controller's
theme.

Change-Id:I680e11902e517ba591a58ad99429f1c05ef31020Author: b533 <b533@borqs.com>
Signed-off-by: Fang, JiuX <jiux.fang@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 25265*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowActivity.java b/src/com/android/mms/ui/SlideshowActivity.java
//Synthetic comment -- index c76b178..f3e4e05 100644

//Synthetic comment -- @@ -29,12 +29,14 @@
import org.w3c.dom.smil.SMILElement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
//Synthetic comment -- @@ -238,7 +240,8 @@
}

private void initMediaController() {
        final Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault);
        mMediaController = new MediaController(dialogContext, false);
mMediaController.setMediaPlayer(new SmilPlayerController(mSmilPlayer));
mMediaController.setAnchorView(findViewById(R.id.slide_view));
mMediaController.setPrevNextListeners(







