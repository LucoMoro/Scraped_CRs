/*Bypass testFormatMethods if device is not using en_US resource.

Change-Id:I915e80cbca27eef563a7242b71a164be9ed90401*/
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








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index a5dd335..c5ed7d5 100644

//Synthetic comment -- @@ -203,6 +203,13 @@
return;
}

long elapsedTime = 2 * 60 * 60;
String expected = "2:00:00";
assertEquals(expected, DateUtils.formatElapsedTime(elapsedTime));







