/*Remove unnecessary reference in PackageManagerService

Change-Id:I55f40b8ded69e10bb940a7c84117c5ff1b988d8eSigned-off-by: You Kim <you.kim72@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 83672c5..6a51913 100644

//Synthetic comment -- @@ -539,7 +539,7 @@
// Already bound to the service. Just make
// sure we trigger off processing the first request.
if (idx == 0) {
                            mHandler.sendEmptyMessage(MCS_BOUND);
}
}
break;
//Synthetic comment -- @@ -574,10 +574,9 @@
if (DEBUG_SD_INSTALL) Log.i(TAG,
"Posting delayed MCS_UNBIND");
removeMessages(MCS_UNBIND);
                                        Message ubmsg = obtainMessage(MCS_UNBIND);
// Unbind after a little delay, to avoid
// continual thrashing.
                                        sendMessageDelayed(ubmsg, 10000);
}
} else {
// There are more pending requests in queue.
//Synthetic comment -- @@ -585,7 +584,7 @@
// of next pending install.
if (DEBUG_SD_INSTALL) Log.i(TAG,
"Posting MCS_BOUND for next woek");
                                    mHandler.sendEmptyMessage(MCS_BOUND);
}
}
}
//Synthetic comment -- @@ -626,7 +625,7 @@
// There are more pending requests in queue.
// Just post MCS_BOUND message to trigger processing
// of next pending install.
                        mHandler.sendEmptyMessage(MCS_BOUND);
}

break;
//Synthetic comment -- @@ -865,7 +864,7 @@
}

processPendingInstall(args, ret);
                        mHandler.sendEmptyMessage(MCS_UNBIND);
}
break;
}
//Synthetic comment -- @@ -903,7 +902,7 @@

processPendingInstall(args, ret);

                        mHandler.sendEmptyMessage(MCS_UNBIND);
}

break;
//Synthetic comment -- @@ -10093,8 +10092,8 @@

/** Called by UserManagerService */
void cleanUpUserLILPw(int userHandle) {
        if (mDirtyUsers.remove(userHandle));
        mSettings.removeUserLPr(userHandle);
if (mInstaller != null) {
// Technically, we shouldn't be doing this with the package lock
// held.  However, this is very rare, and there is already so much







