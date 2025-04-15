/*Enable lid notifications for navigational keyboard

The native api supports retreving lid notification
but the current framework will discard navigational
lid notifications. This makes it hard for gamedevelopers
to rely on the NDK and for them to use the DPAD. The
java api works though.

Change-Id:I6536cb4207cb02083d1cf5864fabefac55fb8bb3*/




//Synthetic comment -- diff --git a/core/java/android/content/res/Resources.java b/core/java/android/content/res/Resources.java
//Synthetic comment -- index e9a2929..e1130a0 100755

//Synthetic comment -- @@ -1298,6 +1298,10 @@
== Configuration.HARDKEYBOARDHIDDEN_YES) {
keyboardHidden = Configuration.KEYBOARDHIDDEN_SOFT;
}

            // Also include navigation hidden information
            keyboardHidden |= mConfiguration.navigationHidden << 2;

mAssets.setConfiguration(mConfiguration.mcc, mConfiguration.mnc,
locale, mConfiguration.orientation,
mConfiguration.touchscreen,







