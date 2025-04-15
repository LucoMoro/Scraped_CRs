/*VibratorService: Fix to ensure actual delay in a vibrate pattern

delay might timeout early as value of duration isn't updated
correctly in the loop, should the wait be interrupted, to reflect
the elapsed time. Fix is to update duration in the loop.

Change-Id:I525b0e97799b288f46ae3a056cff7dcc69701bb0*/




//Synthetic comment -- diff --git a/services/java/com/android/server/VibratorService.java b/services/java/com/android/server/VibratorService.java
//Synthetic comment -- index b609867..72fde11 100755

//Synthetic comment -- @@ -441,7 +441,7 @@

private void delay(long duration) {
if (duration > 0) {
                long bedtime = duration + SystemClock.uptimeMillis();
do {
try {
this.wait(duration);
//Synthetic comment -- @@ -451,8 +451,7 @@
if (mDone) {
break;
}
                    duration = bedtime - SystemClock.uptimeMillis();
} while (duration > 0);
}
}







