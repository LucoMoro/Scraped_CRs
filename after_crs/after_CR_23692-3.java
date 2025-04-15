/*frameworks/base: Fix to pass RESULT_CANCELED for crashed process

Change-Id:Icb631d887d2fac9e12c1c0dc42c07941f6b3888c*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index d24ce7e..ecae852 100644

//Synthetic comment -- @@ -2566,9 +2566,14 @@
if (localLOGV) Slog.v(
TAG, "Removing this entry!  frozen=" + r.haveState
+ " finishing=" + r.finishing);

                    if (r.finishing ||
                        !r.stack.finishActivityLocked(r, i, Activity.RESULT_CANCELED,
                                                      null, "crashed")) {
                        mMainStack.mHistory.remove(i);
                        r.inHistory = false;
                    }

mWindowManager.removeAppToken(r);
if (VALIDATE_TOKENS) {
mWindowManager.validateAppTokens(mMainStack.mHistory);







