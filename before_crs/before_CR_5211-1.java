/*IllegalStateException will be thrown by most media JNI APIs.
It should be caught in AsyncPlayer; otherwise, the activity/service will crash.
(for example, NotificationService)*/
//Synthetic comment -- diff --git a/media/java/android/media/AsyncPlayer.java b/media/java/android/media/AsyncPlayer.java
//Synthetic comment -- index 0620f32..92cc5e7 100644

//Synthetic comment -- @@ -86,6 +86,9 @@
catch (IOException e) {
Log.w(mTag, "error loading sound for " + cmd.uri, e);
}
break;
case STOP:
if (mPlayer != null) {







