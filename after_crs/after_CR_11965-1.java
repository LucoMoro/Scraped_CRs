/*fix issue 2934

Restoring old logic which was OK
Somebody changed logic in DTMFTwelveKeyDialer and TwelveKeyDialer but TwelveKeyDialer was correct*/




//Synthetic comment -- diff --git a/src/com/android/contacts/TwelveKeyDialer.java b/src/com/android/contacts/TwelveKeyDialer.java
//Synthetic comment -- index 33f261e..b7f88a9 100644

//Synthetic comment -- @@ -207,7 +207,7 @@
synchronized (mToneGeneratorLock) {
if (mToneGenerator == null) {
try {
                    mToneGenerator = new ToneGenerator(AudioManager.STREAM_RING, 
TONE_RELATIVE_VOLUME);
} catch (RuntimeException e) {
Log.w(TAG, "Exception caught while creating local tone generator: " + e);
//Synthetic comment -- @@ -381,7 +381,7 @@
synchronized(mToneGeneratorLock) {
if (mToneGenerator == null) {
try {
                    mToneGenerator = new ToneGenerator(AudioManager.STREAM_RING, 
TONE_RELATIVE_VOLUME);
} catch (RuntimeException e) {
Log.w(TAG, "Exception caught while creating local tone generator: " + e);







