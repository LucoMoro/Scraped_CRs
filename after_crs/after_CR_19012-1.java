/*Fix MultiAppStartupTest

Bug 3209163

Remove activities of unbundled packages.

Change-Id:I23c9cd88c1e7c7e09b359ccb1a256b8bf6b0b57d*/




//Synthetic comment -- diff --git a/tests/tests/performance/src/android/performance/cts/MultiAppStartupTest.java b/tests/tests/performance/src/android/performance/cts/MultiAppStartupTest.java
//Synthetic comment -- index d102497..a736d89 100644

//Synthetic comment -- @@ -17,19 +17,10 @@
package android.performance.cts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

public class MultiAppStartupTest extends InstrumentationTestCase {
private static final String PACKAGE_UNDER_TEST = "com.android.calculator2";
private static final String ACTIVITY_UNDER_TEST = "Calculator";
//Synthetic comment -- @@ -80,14 +71,10 @@
Thread.sleep(ACTIVITY_STARTUP_WAIT_TIME);
launchActivity("com.android.mms", "ui.ConversationList", true);
Thread.sleep(ACTIVITY_STARTUP_WAIT_TIME);
launchActivity("com.android.contacts", "TwelveKeyDialer", false);
Thread.sleep(ACTIVITY_STARTUP_WAIT_TIME);
launchActivity("com.android.contacts", "RecentCallsListActivity", false);
Thread.sleep(ACTIVITY_STARTUP_WAIT_TIME);

long finalStartDuration = launchActivityUnderTest();








