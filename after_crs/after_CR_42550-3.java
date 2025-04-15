/*Changed the way to get package/component info.

Some android devices don't expose these info because
other apps can use them to collect user data.
Monkey parameter was changed in order to work properly
with these devices.

Change-Id:I327af7f11995f647f425c76be80b60885a4a3f21*/




//Synthetic comment -- diff --git a/hostsidetests/monkey/src/com/android/cts/monkey/CategoryTest.java b/hostsidetests/monkey/src/com/android/cts/monkey/CategoryTest.java
//Synthetic comment -- index 3932653..3e1d01a 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

public void testDefaultCategories() throws Exception {
String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
	System.out.println("default out = " + out);
assertTrue(out.contains("cmp=com.android.cts.monkey/.MonkeyActivity"));
assertTrue(out.contains("cmp=com.android.cts.monkey/.BaboonActivity"));
}
//Synthetic comment -- @@ -27,20 +28,23 @@
public void testSingleCategory() throws Exception {
String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -c android.intent.category.LAUNCHER 5000");
	System.out.println("single launcher out = " + out);
        assertTrue(out.contains("component=com.android.cts.monkey/.MonkeyActivity"));
        assertFalse(out.contains("component=com.android.cts.monkey/.BaboonActivity"));

out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -c android.intent.category.MONKEY 5000");
	System.out.println("single monkey out = " + out);
        assertFalse(out.contains("component=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("component=com.android.cts.monkey/.BaboonActivity"));
}

public void testMultipleCategories() throws Exception {
String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -c android.intent.category.LAUNCHER"
+ " -c android.intent.category.MONKEY 5000");
	System.out.println("multiple out = " + out);
        assertTrue(out.contains("component=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("component=com.android.cts.monkey/.BaboonActivity"));
}
}








//Synthetic comment -- diff --git a/hostsidetests/monkey/src/com/android/cts/monkey/PackageTest.java b/hostsidetests/monkey/src/com/android/cts/monkey/PackageTest.java
//Synthetic comment -- index aa6106b..bfe575f 100644

//Synthetic comment -- @@ -20,18 +20,18 @@

public void testSinglePackage() throws Exception {
String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0] + " 5000");
        assertTrue(out.contains("component=com.android.cts.monkey/.MonkeyActivity"));
        assertFalse(out.contains("component=com.android.cts.monkey2/.ChimpActivity"));

out = mDevice.executeShellCommand("monkey -v -p " + PKGS[1] + " 5000");
        assertFalse(out.contains("component=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("component=com.android.cts.monkey2/.ChimpActivity"));
}

public void testMultiplePackages() throws Exception {
String out = mDevice.executeShellCommand("monkey -v -p " + PKGS[0]
+ " -p " + PKGS[1] + " 5000");
        assertTrue(out.contains("component=com.android.cts.monkey/.MonkeyActivity"));
        assertTrue(out.contains("component=com.android.cts.monkey2/.ChimpActivity"));
}
}







