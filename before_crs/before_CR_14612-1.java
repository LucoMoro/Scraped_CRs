/*Voicedialer application killed when rotating portrait to landscape has been fixed.

Change-Id:Ia15ee3551ad9f47536a7f6232bf6903393847cea*/
//Synthetic comment -- diff --git a/src/com/android/voicedialer/VoiceDialerActivity.java b/src/com/android/voicedialer/VoiceDialerActivity.java
//Synthetic comment -- index 380d7b7..f2e9a61 100644

//Synthetic comment -- @@ -269,7 +269,7 @@
}

// bye
        finish();
}

private void notifyText(final CharSequence msg) {







