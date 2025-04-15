/*Transparent activity orientation problem when previous landsacpe fullscreen activity not yet destroyed.

After terminating landsacpe fullscreen activity,
when user launch transparent activity via portrait home app, transparent activity is shown as landscape mode.

At this time AppWindowToken of previous acitivity has not been deleted, because Activity.onDestory() has not been returned yet.
In this case, getOrientationFromAppTokensLocked() returned ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE.

Ignore hidden application is terminated on the top.
See alsohttp://code.google.com/p/android/issues/detail?id=28927Change-Id:I51239431120ec6ba8f8ff76871efb2347b9810ca*/




//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3f72dec..c833919

//Synthetic comment -- @@ -3359,17 +3359,7 @@
continue;
}

            if (haveGroup == true && curGroup != wtoken.groupId) {
// If we have hit a new application group, and the bottom
// of the previous group didn't explicitly say to use
// the orientation behind it, and the last app was
//Synthetic comment -- @@ -3382,6 +3372,20 @@
return lastOrientation;
}
}

            // We ignore any hidden applications on the top.
            if (wtoken.hiddenRequested || wtoken.willBeHidden) {
                if (DEBUG_ORIENTATION) Slog.v(TAG, "Skipping " + wtoken
                        + " -- hidden on top");
                continue;
            }

            if (!haveGroup) {
                haveGroup = true;
                curGroup = wtoken.groupId;
                lastOrientation = wtoken.requestedOrientation;
            } 

int or = wtoken.requestedOrientation;
// If this application is fullscreen, and didn't explicitly say
// to use the orientation behind it, then just take whatever







