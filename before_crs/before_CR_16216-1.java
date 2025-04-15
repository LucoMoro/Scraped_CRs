/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:I50cc6c6ebf5db639d4fbee6a513193070de7823e*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index 6bd915a..2742f2f 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
//Synthetic comment -- @@ -226,7 +227,8 @@
mAppWidgetHost.startListening();

if (PROFILE_STARTUP) {
            android.os.Debug.startMethodTracing("/sdcard/launcher");
}

loadHotseats();








//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherModel.java b/src/com/android/launcher2/LauncherModel.java
//Synthetic comment -- index 17f7573..023264d 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
//Synthetic comment -- @@ -581,7 +582,8 @@
}

if (PROFILE_LOADERS) {
                    android.os.Debug.startMethodTracing("/sdcard/launcher-loaders");
}

if (loadWorkspaceFirst) {







