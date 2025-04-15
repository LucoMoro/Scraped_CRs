/*Small GUI fix for PLAY_TONE

Ensured that the activity is visible to the user for at least the
Duration specified in the proactive command.

Change-Id:Ib5b3d61f198041764c6240dcaedc6c52909d045bSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/stk/ToneDialog.java b/src/com/android/stk/ToneDialog.java
//Synthetic comment -- index ba4a957..95e7b4e 100644

//Synthetic comment -- @@ -82,6 +82,9 @@
iv.setImageBitmap(toneMsg.icon);
}

        // Ensure that the activity is visible during the complete duration
        setVisible(true);

// Start playing tone and vibration
player = new TonePlayer();
player.play(settings.tone);







