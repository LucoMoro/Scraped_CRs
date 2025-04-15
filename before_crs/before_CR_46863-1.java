/*remove leak WaitResult List in startActivityMayWait

Change-Id:If169ef4474a6a38eb2957c806c6e7f8f7ce67783Signed-off-by: Young-ho Cha <ganadist@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 4546dc3..7da1b5b 100644

//Synthetic comment -- @@ -3201,6 +3201,7 @@
} catch (InterruptedException e) {
}
} while (!outResult.timeout && outResult.who == null);
} else if (res == ActivityManager.START_TASK_TO_FRONT) {
ActivityRecord r = this.topRunningActivityLocked(null);
if (r.nowVisible) {
//Synthetic comment -- @@ -3217,6 +3218,7 @@
} catch (InterruptedException e) {
}
} while (!outResult.timeout && outResult.who == null);
}
}
}







