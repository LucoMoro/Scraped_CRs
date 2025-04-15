/*JAVA Crash in Google Map when run Monkey test

Rootcause: If the device have hardware key,When the Monkey test
send the KEY_MENU event quickly,Before the parent window dispatch
it's token to it's view. If the KEY_MENU event come.The sub window
will show, and it will don't get the correct token from parent window.
Then it crash.

Solution: This is a workround fix.Before show the sub window.we do a
check first.if the token got from parent is incorrect,stop the process.

Change-Id:I5ed5f431f80ba9532be9d960689a62ba694df57fAuthor: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61569*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindow.java b/policy/src/com/android/internal/policy/impl/PhoneWindow.java
//Synthetic comment -- index 41d67bc..cee78ed 100644

//Synthetic comment -- @@ -53,6 +53,7 @@
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
//Synthetic comment -- @@ -799,6 +800,14 @@
* @param event The key event.
*/
public final void onKeyUpPanel(int featureId, KeyEvent event) {
// The panel key was released, so clear the chording key
if (mPanelChordingKey != 0) {
mPanelChordingKey = 0;







