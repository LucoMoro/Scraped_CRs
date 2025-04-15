/*ADT Junit test: Check for array size before accessing items

Change-Id:Ia6682382a445ca770691771f620a10b7d408c152*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/JUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/JUnitLaunchConfigDelegate.java
//Synthetic comment -- index 442d356..a2da94d 100644

//Synthetic comment -- @@ -72,13 +72,13 @@

/**
* Removes the android.jar from the bootstrap path if present.
     *
* @param bootpath Array of Arrays of bootstrap class paths
* @return a new modified (if applicable) bootpath
*/
public static String[][] fixBootpathExt(String[][] bootpath) {
for (int i = 0; i < bootpath.length; i++) {
            if (bootpath[i] != null && bootpath[i].length > 0) {
// we assume that the android.jar can only be present in the
// bootstrap path of android tests
if (bootpath[i][0].endsWith(SdkConstants.FN_FRAMEWORK_LIBRARY)) {
//Synthetic comment -- @@ -93,16 +93,16 @@
* Add the junit.jar to the user classpath; since Eclipse was relying on
* android.jar to provide the appropriate org.junit classes, it does not
* know it actually needs the junit.jar.
     *
     * @param classpath Array containing classpath
     * @param projectName The name of the project (for logging purposes)
     *
* @return a new modified (if applicable) classpath
*/
public static String[] fixClasspath(String[] classpath, String projectName) {
// search for junit.jar; if any are found return immediately
for (int i = 0; i < classpath.length; i++) {
            if (classpath[i].endsWith(JUNIT_JAR)) {
return classpath;
}
}
//Synthetic comment -- @@ -138,9 +138,9 @@

/**
* Returns the path of the junit jar in the highest version bundle.
     *
* (This is public only so that the test can call it)
     *
* @return the path as a string
* @throws IOException
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/launch/JUnitLaunchConfigDelegateTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/launch/JUnitLaunchConfigDelegateTest.java
//Synthetic comment -- index f393ebf..58f686a 100644

//Synthetic comment -- @@ -27,22 +27,24 @@
public void testAbleToFetchJunitJar() throws IOException {
assertTrue(JUnitLaunchConfigDelegate.getJunitJarLocation().endsWith("junit.jar"));
}

public void testFixBootpathExtWithAndroidJar() {
String[][] testArray = {
null,
                {},
{ "android.jar"},
null,
{ "some_other_jar.jar" },
};

String[][] expectedArray = {
null,
                {},
null,
null,
{ "some_other_jar.jar" },
};

assertEqualsArrays(expectedArray, JUnitLaunchConfigDelegate.fixBootpathExt(testArray));
}

//Synthetic comment -- @@ -52,13 +54,13 @@
{ "somejar.jar"},
null,
};

String[][] expectedArray = {
null,
{ "somejar.jar"},
null,
};

assertEqualsArrays(expectedArray, JUnitLaunchConfigDelegate.fixBootpathExt(testArray));
}

//Synthetic comment -- @@ -66,26 +68,26 @@
String[] testArray = {
JUnitLaunchConfigDelegate.getJunitJarLocation(),
};

String[] expectedArray = {
JUnitLaunchConfigDelegate.getJunitJarLocation(),
};

        assertEqualsArrays(expectedArray,
JUnitLaunchConfigDelegate.fixClasspath(testArray, "test"));
}

public void testFixClasspathWithoutJunitJar() throws IOException {
String[] testArray = {
"random.jar",
};

String[] expectedArray = {
"random.jar",
JUnitLaunchConfigDelegate.getJunitJarLocation(),
};

        assertEqualsArrays(expectedArray,
JUnitLaunchConfigDelegate.fixClasspath(testArray, "test"));
}

//Synthetic comment -- @@ -93,20 +95,20 @@
public void testFixClasspathWithNoJars() throws IOException {
String[] testArray = {
};

String[] expectedArray = {
JUnitLaunchConfigDelegate.getJunitJarLocation(),
};

        assertEqualsArrays(expectedArray,
JUnitLaunchConfigDelegate.fixClasspath(testArray, "test"));
}

private void assertEqualsArrays(String[][] a1, String[][] a2) {
        assertTrue(Arrays.deepEquals(a1, a2));
}

private void assertEqualsArrays(String[] a1, String[] a2) {
        assertTrue(Arrays.deepEquals(a1, a2));
}
}







