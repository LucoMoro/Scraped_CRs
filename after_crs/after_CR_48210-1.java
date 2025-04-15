/*clipboard access control implementation

The uses the SELinux object class x_application_data and permission paste to
determine if an app can paste data copied from another app.

In the future this check can be replaced by a query to a framework security server
instead of the SELinux security server.

Change-Id:Ib0b6aeca59511ce71832aee1afd4150d1514a63c*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ClipboardService.java b/services/java/com/android/server/ClipboardService.java
//Synthetic comment -- index 74ec6e2..f013f22 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SELinux;
import android.os.UserHandle;
import android.util.Pair;
import android.util.Slog;
//Synthetic comment -- @@ -64,6 +65,7 @@
= new RemoteCallbackList<IOnPrimaryClipChangedListener>();

ClipData primaryClip;
        String securityLabel;

final HashSet<String> activePermissionOwners
= new HashSet<String>();
//Synthetic comment -- @@ -146,6 +148,8 @@
clearActiveOwnersLocked();
PerUserClipboard clipboard = getClipboard();
clipboard.primaryClip = clip;
            clipboard.securityLabel = SELinux.getPidContext(Binder.getCallingPid());
            Slog.i(TAG, "Got clip for securityLabel=" + clipboard.securityLabel);
final int n = clipboard.primaryClipListeners.beginBroadcast();
for (int i = 0; i < n; i++) {
try {
//Synthetic comment -- @@ -163,6 +167,12 @@
public ClipData getPrimaryClip(String pkg) {
synchronized (this) {
addActiveOwnerLocked(Binder.getCallingUid(), pkg);

            // Did not add this to addActiveOwnerLocked because throwing an exception
            // for an SELinux violation kills the app and is generally not desirable
            if (!checkSELinuxAccess())
                return null;

return getClipboard().primaryClip;
}
}
//Synthetic comment -- @@ -170,12 +180,18 @@
public ClipDescription getPrimaryClipDescription() {
synchronized (this) {
PerUserClipboard clipboard = getClipboard();

            if (!checkSELinuxAccess())
                return null;

return clipboard.primaryClip != null ? clipboard.primaryClip.getDescription() : null;
}
}

public boolean hasPrimaryClip() {
synchronized (this) {
            if (!checkSELinuxAccess())
                return false;
return getClipboard().primaryClip != null;
}
}
//Synthetic comment -- @@ -194,6 +210,8 @@

public boolean hasClipboardText() {
synchronized (this) {
            if (!checkSELinuxAccess())
                return false;
PerUserClipboard clipboard = getClipboard();
if (clipboard.primaryClip != null) {
CharSequence text = clipboard.primaryClip.getItemAt(0).getText();
//Synthetic comment -- @@ -316,4 +334,11 @@
revokeItemLocked(clipboard.primaryClip.getItemAt(i));
}
}

    private final boolean checkSELinuxAccess() {
            String securityLabel = SELinux.getPidContext(Binder.getCallingPid());
            Slog.i(TAG, "Get clip for securityLabel=" + securityLabel);
            return SELinux.checkSELinuxAccess(securityLabel, getClipboard().securityLabel, "x_application_data", "paste");
    }

}







