/*Closing cursor in finalizer to avoid GREF and fd leak in acore

The finalize() call did not clean up completely, this eventually
caused the android.process.acore to crash since it ran out of fds
and GREF to increased above 2000 if an application forgot to close
its cursor objects. A warning was also added when this happens so
that application developers can correct their mistake. The
included test case tries to verify that the finalizer works as
expected by creating a bunch of Cursor objects without closing
them (without this fix the acore process crashes after about 400
iterations and the test case ends with "Process crashed").

Change-Id:I11e485cef1ac02e718b2742108aa88793666c31d*/




//Synthetic comment -- diff --git a/core/java/android/content/ContentResolver.java b/core/java/android/content/ContentResolver.java
//Synthetic comment -- index 1f3426e..d16b3d8 100644

//Synthetic comment -- @@ -1398,9 +1398,11 @@

@Override
protected void finalize() throws Throwable {
            // TODO: integrate CloseGuard support.
try {
if(!mCloseFlag) {
                    Log.w(TAG, "Cursor finalized without prior close()");
                    close();
}
} finally {
super.finalize();








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/content/ContentResolverTest.java b/core/tests/coretests/src/android/content/ContentResolverTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2b6dee8b

//Synthetic comment -- @@ -0,0 +1,41 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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
package android.content;

import android.content.ContentResolver;
import android.provider.ContactsContract;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;

public class ContentResolverTest extends AndroidTestCase {
    private ContentResolver mContentResolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContentResolver = mContext.getContentResolver();
    }

    @LargeTest
    public void testCursorFinalizer() throws Exception {
        // TODO: Want a test case that more predictably reproduce this issue. Selected
        // 600 as this causes the problem 100% of the runs on current hw, it might not
        // do so on some other configuration though.
        for (int i = 0; i < 600; i++) {
            mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        }
    }
}







