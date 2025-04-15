/*Play ringtone picker previews through the music stream.

The previous behavior was to always play the ringtone through the
speaker, as is the case when the phone is actually ringing. However, it
is safe to assume that the user is actually at the device when
previewing ringtones, obviating the need to play though the speaker and
potentially causing embarrassment when using headphones.

Fixes AOSP issue 16174.

Change-Id:I82aab8e2d93f5094de4f068b814f1e592454f92e*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/app/RingtonePickerActivity.java b/core/java/com/android/internal/app/RingtonePickerActivity.java
//Synthetic comment -- index 5569ffe..f1e7f61 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
//Synthetic comment -- @@ -309,6 +310,7 @@
}

if (ringtone != null) {
ringtone.play();
}
}







