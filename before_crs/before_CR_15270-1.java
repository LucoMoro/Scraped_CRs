/*Telephony : Dial emergency phone numbers when screen is locked

The current implementation do not allow dialing emergency phone
numbers with the qwerty-keyboard when the screen is locked.

This contribution will automatically start the emergency dialer
if a valid digit (i.e. 0-9,*,+,#) is entered from the qwerty
keypad when the screen is locked. The emergency dialer appears
pre-filled with the first entered digit.

Change-Id:I136b5434f99bfab6f1908d3698e2700f7e59d036*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardScreenCallback.java b/phone/com/android/internal/policy/impl/KeyguardScreenCallback.java
//Synthetic comment -- index 6bb6a45..1c4a462 100644

//Synthetic comment -- @@ -63,6 +63,12 @@
void takeEmergencyCallAction();

/**
* Report that the user had a failed attempt unlocking via the pattern.
*/
void reportFailedPatternAttempt();








//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java b/phone/com/android/internal/policy/impl/LockPatternKeyguardView.java
//Synthetic comment -- index 85918fb..fded21e 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
//Synthetic comment -- @@ -245,9 +247,19 @@
}

public void takeEmergencyCallAction() {
Intent intent = new Intent(ACTION_EMERGENCY_DIAL);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
getContext().startActivity(intent);
}

//Synthetic comment -- @@ -436,6 +448,21 @@
}

@Override
public void verifyUnlock() {
if (!isSecure()) {
// non-secure keyguard screens are successfull by default







