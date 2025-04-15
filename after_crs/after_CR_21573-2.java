/*frameworks/base: unlink death notifications of Vibrate requests

Death notifications of Vibrate requests isn't needed when
broadcast of ACTION_SCREEN_OFF is received.

Change-Id:Ic411525fa8ce1fce3fa215314c54440ce837e41a*/




//Synthetic comment -- diff --git a/services/java/com/android/server/VibratorService.java b/services/java/com/android/server/VibratorService.java
//Synthetic comment -- index 86c30f8..71353911 100755

//Synthetic comment -- @@ -379,6 +379,12 @@
if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
synchronized (mVibrations) {
doCancelVibrateLocked();

                    int size = mVibrations.size();
                    for(int i = 0; i < size; i++) {
                        unlinkVibration(mVibrations.get(i));
                    }

mVibrations.clear();
}
}







