/*fix test for non-telephony devices

check for the presence of the telephony feature
before testing for the call.

Change-Id:I2989033fcb163d1b1440806ad7a9a5dc501318d3*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
//Synthetic comment -- index 6e86967..4ea76f4 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.permission.cts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
//Synthetic comment -- @@ -70,14 +71,19 @@
*/
@SmallTest
public void testCall911() {
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            //Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED, Uri.parse("tel:911"));
            Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED",
                    Uri.parse("tel:911"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(intent);
                fail("startActivity(Intent.ACTION_CALL_PRIVILEGED) did not throw " +
                		"SecurityException as expected");
            } catch (SecurityException e) {
               // expected
           }
}
}








