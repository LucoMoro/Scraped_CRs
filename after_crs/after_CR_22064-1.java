/*frameworks/base: Fix to ensure actual delay in a vibrate pattern

The function delay times out soon when InterruptedException is
raised during the wait() call, as the value of bedtime is not
updated in the while loop. Fix is to update the same in the while
loop.

  It might also timeout as value of duration isn't updated properly
to reflect the elapsed time.

Change-Id:I2ecc452425f9cb18f19fdaa8a7fa45aa9f30b3f0*/




//Synthetic comment -- diff --git a/services/java/com/android/server/VibratorService.java b/services/java/com/android/server/VibratorService.java
//Synthetic comment -- index 86c30f8..47e6b662 100755

//Synthetic comment -- @@ -304,8 +304,8 @@

private void delay(long duration) {
if (duration > 0) {
do {
                    long bedtime = SystemClock.uptimeMillis();
try {
this.wait(duration);
}
//Synthetic comment -- @@ -315,7 +315,7 @@
break;
}
duration = duration
                            - (SystemClock.uptimeMillis() - bedtime);
} while (duration > 0);
}
}







