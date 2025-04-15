/*Removing More PackageManager Permission Tests

Bug 2750299

There was another PackageManagerTest in addition to the previously
fixed PackageManagerRequiringPermissionsTest that was testing that
a SecurityException should be thrown. However, it is not expected
to throw that anymore.

Change-Id:I2e6c2556fdaf85f8f0b7adbbac531854e958d192*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/pm/cts/PackageManagerTest.java b/tests/tests/content/src/android/content/pm/cts/PackageManagerTest.java
//Synthetic comment -- index 337f0a0..1857f13 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
//Synthetic comment -- @@ -37,8 +36,6 @@
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.AndroidTestCase;

import java.util.ArrayList;
//Synthetic comment -- @@ -634,51 +631,6 @@
// Can't add permission in dynamic way
}

@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,







