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

import android.content.pm.IPackageInstallObserver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
//Synthetic comment -- @@ -37,8 +36,6 @@
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.provider.Contacts.People;
import android.test.AndroidTestCase;

import java.util.ArrayList;
//Synthetic comment -- @@ -634,51 +631,6 @@
// Can't add permission in dynamic way
}

    /**
     * Test that calling {@link PackageManager#addPackageToPreferred(String) throws a
     * {@link SecurityException}.
     * <p/>
     * The method is protected by the {@link android.permission.SET_PREFERRED_APPLICATIONS}
     * signature permission. Even though this app declares that permission, it still should not be
     * able to call this method.
     */
    public void testAddPackageToPreferred() {
        // Test addPackageToPreferred, getPreferredPackages
        List<PackageInfo> pkgInfo = null;
        pkgInfo = mPackageManager.getPreferredPackages(0);
        int pkgInfoSize = pkgInfo.size();
        try {
            // addPackageToPreferred is protected by the
            // android.permission.SET_PREFERRED_APPLICATIONS signature permission.
            // Even though this app declares that permission, it still should not be able to call
            // this
            mPackageManager.addPackageToPreferred(CONTENT_PKG_NAME);
            fail("addPackageToPreferred unexpectedly succeeded");
        } catch (SecurityException e) {
            // expected
        }
        pkgInfo = mPackageManager.getPreferredPackages(0);
        // addPackageToPreferred should have no effect
        assertEquals(pkgInfo.size(), pkgInfoSize);
    }

    /**
     * Test that calling {@link PackageManager#removePackageFromPreferred(String)} throws a
     * {@link SecurityException}.
     * <p/>
     * The method is protected by the {@link android.permission.SET_PREFERRED_APPLICATIONS}
     * signature permission. Even though this app declares that permission, it still should not be
     * able to call this method.
     */
    public void testRemovePackageFromPreferred() {
        try {
            mPackageManager.removePackageFromPreferred(CONTENT_PKG_NAME);
            fail("removePackageFromPreferred unexpectedly succeeded");
        } catch (SecurityException e) {
            // expected
        }
    }

@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,







