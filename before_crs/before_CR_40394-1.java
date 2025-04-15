/*VibratorService: Fix to ensure actual delay in a vibrate pattern

The function delay times out early when InterruptedException is
raised during the wait() call, as the value of bedtime is not
updated in the while loop. Fix is to update the same in the while
loop.

  It might also timeout early as value of duration isn't updated
correctly to reflect the elapsed time.

Change-Id:I525b0e97799b288f46ae3a056cff7dcc69701bb0*/
//Synthetic comment -- diff --git a/services/java/com/android/server/VibratorService.java b/services/java/com/android/server/VibratorService.java
//Synthetic comment -- index b609867..77829e1 100755

//Synthetic comment -- @@ -441,8 +441,8 @@

private void delay(long duration) {
if (duration > 0) {
                long bedtime = SystemClock.uptimeMillis();
do {
try {
this.wait(duration);
}
//Synthetic comment -- @@ -452,7 +452,7 @@
break;
}
duration = duration
                            - SystemClock.uptimeMillis() - bedtime;
} while (duration > 0);
}
}







