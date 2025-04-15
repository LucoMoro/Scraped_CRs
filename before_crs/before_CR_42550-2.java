/*Changed the way to get package/component info.

Some android devices don't expose these info because
other apps can use them to collect user data.
Monkey parameter was changed in order to work properly
with these devices. Now all required info is retrieved
from Monkey app itself instead of through activity manager.

Change-Id:I327af7f11995f647f425c76be80b60885a4a3f21*/
//Synthetic comment -- diff --git a/hostsidetests/monkey/src/com/android/cts/monkey/CategoryTest.java b/hostsidetests/monkey/src/com/android/cts/monkey/CategoryTest.java
//Synthetic comment -- index 3932653..82f334e 100644

//Synthetic comment -- @@ -19,28 +19,28 @@
public class CategoryTest extends AbstractMonkeyTest {

public void testDefaultCategories() throws Exception {
        String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
        assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testSingleCategory() throws Exception {
        String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -c android.intent.category.LAUNCHER 5000");
        assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertFalse(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));

        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -c android.intent.category.MONKEY 5000");
        assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}

public void testMultipleCategories() throws Exception {
        String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -c android.intent.category.LAUNCHER"
+ " -c android.intent.category.MONKEY 5000");
        assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}
}








//Synthetic comment -- diff --git a/hostsidetests/monkey/src/com/android/cts/monkey/PackageTest.java b/hostsidetests/monkey/src/com/android/cts/monkey/PackageTest.java
//Synthetic comment -- index aa6106b..27824aa 100644

//Synthetic comment -- @@ -19,19 +19,19 @@
public class PackageTest extends AbstractMonkeyTest {

public void testSinglePackage() throws Exception {
        String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
        assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertFalse(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));

        out = mDevice.executeShellCommand("monkey -v -p " + PKGS[1] + " 5000");
        assertFalse(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}

public void testMultiplePackages() throws Exception {
        String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -p " + PKGS[1] + " 5000");
        assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("cmp=com.android.cts.monkey2/.ChimpActivity"));
}
}







