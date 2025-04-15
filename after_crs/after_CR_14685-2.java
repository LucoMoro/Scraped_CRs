/*Porting Unsubmitted Cupcake CookieSyncManagerTest

Didn't need CtsTestServer as suggested by removing
dependencies on WebView.

Squashed commit of the following:

commit 8acc230e27549bee0dedf9fea4bf0e6789950876
Date:   Wed Nov 11 12:22:11 2009 -0800

    Integrate unsubmitted cupcake change 132847:
    	CTS: add test cases for android.webkit.CookieSyncManager

    Change-Id:Ibcb9299c9c3a720a09027c0514fa7b23979b9a40Change-Id:I6ff30eba69c493531212a3d344bd3f75c8e238be*/




//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/CookieSyncManagerTest.java b/tests/tests/webkit/src/android/webkit/cts/CookieSyncManagerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2f4c35a

//Synthetic comment -- @@ -0,0 +1,95 @@
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

package android.webkit.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.animation.cts.DelayedCheck;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

@TestTargetClass(android.webkit.CookieSyncManager.class)
public class CookieSyncManagerTest
        extends ActivityInstrumentationTestCase2<CookieSyncManagerStubActivity> {

    public CookieSyncManagerTest() {
        super("com.android.cts.stub", CookieSyncManagerStubActivity.class);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createInstance",
            args = {Context.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getInstance",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "sync",
            args = {}
        )
    })
    public void testCookieSyncManager() {
        CookieSyncManager csm1 = CookieSyncManager.createInstance(getActivity());
        assertNotNull(csm1);

        CookieSyncManager csm2 = CookieSyncManager.getInstance();
        assertNotNull(csm2);

        assertSame(csm1, csm2);

        final CookieManager cookieManager = CookieManager.getInstance();
        assertFalse(cookieManager.hasCookies());

        cookieManager.setAcceptCookie(true);
        assertTrue(cookieManager.acceptCookie());

        String cookieValue = "a = b";
        cookieManager.setCookie(TestHtmlConstants.HELLO_WORLD_URL, cookieValue);
        assertEquals(cookieValue, cookieManager.getCookie(TestHtmlConstants.HELLO_WORLD_URL));

        // Cookie is stored in RAM but not in the database.
        assertFalse(cookieManager.hasCookies());

        // Store the cookie to the database.
        csm1.sync();
        new DelayedCheck(10000) {
            @Override
            protected boolean check() {
                return cookieManager.hasCookies();
            }
        }.run();

        // Remove all cookies from the database.
        cookieManager.removeAllCookie();
        new DelayedCheck(10000) {
            @Override
            protected boolean check() {
                return !cookieManager.hasCookies();
            }
        }.run();
    }
}







