/*Remove unnecessary references inside of Handlers.

Some Handlers are using unnecessary references.
Because the field mHandler - or mH - is Handler itself.
Looks like these codes were copied from outside of it.
This will remove dependancies and make code simpler.

Change-Id:I4bac7b027c53e0bec1f5e1143388f73f87d9fdd5Signed-off-by: You Kim <you.kim72@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/UiModeManagerService.java b/services/java/com/android/server/UiModeManagerService.java
//Synthetic comment -- index d1f92a7..b3e6a52 100644

//Synthetic comment -- @@ -747,9 +747,9 @@
}
Bundle bundle = new Bundle();
bundle.putLong(KEY_LAST_UPDATE_INTERVAL, interval);
                        Message newMsg = mHandler.obtainMessage(MSG_ENABLE_LOCATION_UPDATES);
newMsg.setData(bundle);
                        mHandler.sendMessageDelayed(newMsg, interval);
}
break;
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 5d74cf3..43366d4 100644

//Synthetic comment -- @@ -1008,9 +1008,9 @@
case SERVICE_TIMEOUT_MSG: {
if (mDidDexOpt) {
mDidDexOpt = false;
                    Message nmsg = mHandler.obtainMessage(SERVICE_TIMEOUT_MSG);
nmsg.obj = msg.obj;
                    mHandler.sendMessageDelayed(nmsg, SERVICE_TIMEOUT);
return;
}
serviceTimeout((ProcessRecord)msg.obj);
//Synthetic comment -- @@ -1080,7 +1080,7 @@
d.setTitle(title);
d.setMessage(text);
d.setButton(DialogInterface.BUTTON_POSITIVE, "I'm Feeling Lucky",
                            mHandler.obtainMessage(IM_FEELING_LUCKY_MSG));
mUidAlert = d;
d.show();
}
//Synthetic comment -- @@ -1094,9 +1094,9 @@
case PROC_START_TIMEOUT_MSG: {
if (mDidDexOpt) {
mDidDexOpt = false;
                    Message nmsg = mHandler.obtainMessage(PROC_START_TIMEOUT_MSG);
nmsg.obj = msg.obj;
                    mHandler.sendMessageDelayed(nmsg, PROC_START_TIMEOUT);
return;
}
ProcessRecord app = (ProcessRecord)msg.obj;








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index b9e63b7..0cd004d 100755

//Synthetic comment -- @@ -335,9 +335,9 @@
case IDLE_TIMEOUT_MSG: {
if (mService.mDidDexOpt) {
mService.mDidDexOpt = false;
                        Message nmsg = mHandler.obtainMessage(IDLE_TIMEOUT_MSG);
nmsg.obj = msg.obj;
                        mHandler.sendMessageDelayed(nmsg, IDLE_TIMEOUT);
return;
}
// We don't at this point know if the activity is fullscreen,
//Synthetic comment -- @@ -369,8 +369,8 @@
case LAUNCH_TIMEOUT_MSG: {
if (mService.mDidDexOpt) {
mService.mDidDexOpt = false;
                        Message nmsg = mHandler.obtainMessage(LAUNCH_TIMEOUT_MSG);
                        mHandler.sendMessageDelayed(nmsg, LAUNCH_TIMEOUT);
return;
}
synchronized (mService) {








//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index f914271..718dc94 100644

//Synthetic comment -- @@ -513,7 +513,7 @@
// Already bound to the service. Just make
// sure we trigger off processing the first request.
if (idx == 0) {
                            mHandler.sendEmptyMessage(MCS_BOUND);
}
}
break;
//Synthetic comment -- @@ -559,7 +559,7 @@
// of next pending install.
if (DEBUG_SD_INSTALL) Log.i(TAG,
"Posting MCS_BOUND for next woek");
                                    mHandler.sendEmptyMessage(MCS_BOUND);
}
}
}
//Synthetic comment -- @@ -600,7 +600,7 @@
// There are more pending requests in queue.
// Just post MCS_BOUND message to trigger processing
// of next pending install.
                        mHandler.sendEmptyMessage(MCS_BOUND);
}

break;
//Synthetic comment -- @@ -772,7 +772,7 @@
int ret = PackageManager.INSTALL_FAILED_VERIFICATION_TIMEOUT;
processPendingInstall(args, ret);

                        mHandler.sendEmptyMessage(MCS_UNBIND);
}

break;
//Synthetic comment -- @@ -809,7 +809,7 @@

processPendingInstall(args, ret);

                        mHandler.sendEmptyMessage(MCS_UNBIND);
}

break;








//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowManagerService.java b/services/java/com/android/server/wm/WindowManagerService.java
//Synthetic comment -- index 7011343..ead3f56 100755

//Synthetic comment -- @@ -7195,7 +7195,7 @@
if (mAnimationScheduled) {
// If we are animating, don't do the gc now but
// delay a bit so we don't interrupt the animation.
                            mH.sendMessageDelayed(mH.obtainMessage(H.FORCE_GC),
2000);
return;
}
//Synthetic comment -- @@ -7342,7 +7342,7 @@
}

if (doRequest) {
                            mH.sendEmptyMessage(CLEAR_PENDING_ACTIONS);
performLayoutAndPlaceSurfacesLocked();
}
}







