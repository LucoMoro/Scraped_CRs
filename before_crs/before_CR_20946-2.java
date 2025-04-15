/*CTS : remove testCallVoicemail

Comment from btmura : Test is invalid, because the CDD doesn't say
what is required in terms of permissions by the applications that
respond to these intents. Also the Dialer isn't part of the core
systems application list defined under the CDD's 3.2.3.1. Finally,
it appears that Intent.ACTION_CALL_PRIVILEGED is a private API as
well.

Change-Id:I9dc661d8f35b0960e79061232014eee51e54410b*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
deleted file mode 100644
//Synthetic comment -- index 88d5f1c..0000000

//Synthetic comment -- @@ -1,96 +0,0 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.permission.cts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Verify Phone calling related methods without specific Phone/Call permissions.
 */
public class NoCallPermissionTest extends AndroidTestCase {

    /**
     * Verify that Intent.ACTION_CALL requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#CALL_PHONE}.
     */
    @SmallTest
    public void testActionCall() {
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Uri uri = Uri.parse("tel:123456");
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(intent);
                fail("startActivity(Intent.ACTION_CALL) did not throw SecurityException as expected");
            } catch (SecurityException e) {
                // expected
            }
        }
    }

    /**
     * Verify that Intent.ACTION_CALL_PRIVILEGED requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#CALL_PRIVILEGED}.
     */
    @SmallTest
    public void testCallVoicemail() {
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            try {
                //Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
                Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED",
                        Uri.fromParts("voicemail", "", null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                fail("startActivity(Intent.ACTION_CALL_PRIVILEGED) did not throw SecurityException as expected");
            } catch (SecurityException e) {
                // expected
            }
        }
     }

    /**
     * Verify that Intent.ACTION_CALL_PRIVILEGED requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#CALL_PRIVILEGED}.
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

}







