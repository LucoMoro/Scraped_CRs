/*Fix voice dialer causes null pointer exception

Press some search results would cause null pointer exception.

Change-Id:I80bfd7a2e8f573e9f848d823f43c50c57555fa0a*/
//Synthetic comment -- diff --git a/src/com/android/voicedialer/VoiceDialerActivity.java b/src/com/android/voicedialer/VoiceDialerActivity.java
//Synthetic comment -- index 9adba6c..c913070 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.media.ToneGenerator;
//Synthetic comment -- @@ -576,7 +577,11 @@
private void startActivityHelp(Intent intent) {
if (getArg(MICROPHONE_EXTRA) == null &&
getArg(CONTACTS_EXTRA) == null) {
            startActivity(intent);
} else {
notifyText(intent.
getStringExtra(RecognizerEngine.SENTENCE_EXTRA) +







