/*Fallback to fallbackring if ringtone can't be played.

If the specified ringtone file exists but cannot be played
the Ringtone will try to play the ringtone in a remote player.
But since the file is corrupt it will not be played in the
remote player either.
According to the documentation it should attempt to
fallback on another sound. This commit will do that.

Change-Id:I0216d61ca874eef0f168ad4d5bfb07491e01e654*/
//Synthetic comment -- diff --git a/media/java/android/media/Ringtone.java b/media/java/android/media/Ringtone.java
//Synthetic comment -- index f190eb9..ebbfad9 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
//Synthetic comment -- @@ -229,10 +231,14 @@
try {
mRemotePlayer.play(mRemoteToken, canonicalUri, mStreamType);
} catch (RemoteException e) {
                Log.w(TAG, "Problem playing ringtone: " + e);
}
} else {
            Log.w(TAG, "Neither local nor remote playback available");
}
}

//Synthetic comment -- @@ -280,6 +286,43 @@
}
}

void setTitle(String title) {
mTitle = title;
}







