/*Fixed system-crash when displaying account-list.

Catch exception thrown when icon/label is not set in account
authenticator.

Change-Id:Ie133a84c802e6d72f8a1ffd9202bcce48e60ba17*/
//Synthetic comment -- diff --git a/src/com/android/settings/AccountPreferenceBase.java b/src/com/android/settings/AccountPreferenceBase.java
//Synthetic comment -- index 520b9d7..c1b3c41 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.content.SyncAdapterType;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -132,12 +133,16 @@
protected Drawable getDrawableForType(final String accountType) {
Drawable icon = null;
if (mTypeToAuthDescription.containsKey(accountType)) {
try {
                AuthenticatorDescription desc = (AuthenticatorDescription)
                        mTypeToAuthDescription.get(accountType);
                Context authContext = createPackageContext(desc.packageName, 0);
                icon = authContext.getResources().getDrawable(desc.iconId);
} catch (PackageManager.NameNotFoundException e) {
// TODO: place holder icon for missing account icons?
Log.w(TAG, "No icon for account type " + accountType);
}
//Synthetic comment -- @@ -153,14 +158,18 @@
protected CharSequence getLabelForType(final String accountType) {
CharSequence label = null;
if (mTypeToAuthDescription.containsKey(accountType)) {
             try {
                 AuthenticatorDescription desc = (AuthenticatorDescription)
                         mTypeToAuthDescription.get(accountType);
                 Context authContext = createPackageContext(desc.packageName, 0);
                 label = authContext.getResources().getText(desc.labelId);
             } catch (PackageManager.NameNotFoundException e) {
                 Log.w(TAG, "No label for account type " + ", type " + accountType);
             }
}
return label;
}







