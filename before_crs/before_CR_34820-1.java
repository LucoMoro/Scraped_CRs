/*Low battery warning during call

This is a change to make the low battery warning play during a call.
Currently the low battery sound will not trigger if the phone is in a
call when it is supposed to play.

Change-Id:Ic34f2090f33922cf9ee42b6e51f2cb18c12ba558*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/power/PowerUI.java b/packages/SystemUI/src/com/android/systemui/power/PowerUI.java
//Synthetic comment -- index fe7d5aa..dec8d53 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.systemui.R;
import com.android.systemui.SystemUI;
//Synthetic comment -- @@ -233,6 +234,9 @@
}

final ContentResolver cr = mContext.getContentResolver();
if (Settings.System.getInt(cr, Settings.System.POWER_SOUNDS_ENABLED, 1) == 1) {
final String soundPath = Settings.System.getString(cr,
Settings.System.LOW_BATTERY_SOUND);
//Synthetic comment -- @@ -241,7 +245,10 @@
if (soundUri != null) {
final Ringtone sfx = RingtoneManager.getRingtone(mContext, soundUri);
if (sfx != null) {
                        sfx.setStreamType(AudioManager.STREAM_NOTIFICATION);
sfx.play();
}
}







