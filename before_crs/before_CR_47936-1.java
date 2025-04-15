/*Unit test fix: make tests more resilient to test jar location

Also handle case where $ANDROID_BUILD_TOP is set better.

Change-Id:Ib39f275417738af9145697e19f4de085255c1655*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index d54962a..f57fb9c 100644

//Synthetic comment -- @@ -376,18 +376,15 @@
try {
File dir = new File(location.toURI());
assertTrue(dir.getPath(), dir.exists());
                File rootDir = dir.getParentFile().getParentFile().getParentFile()
                        .getParentFile().getParentFile().getParentFile();

                // check if "settings.gradle" is there. This will let us know if we need
                // to go up one extra level, which is the case when running the tests
                // from gradle.
                File settingsGradle = new File(rootDir, "settings.gradle"); //$NON-NLS-1$
                if (settingsGradle.isFile()) {
                    rootDir = rootDir.getParentFile();
}

                return rootDir;
} catch (URISyntaxException e) {
fail(e.getLocalizedMessage());
}








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java
//Synthetic comment -- index 9705dff..5d47c5c 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
}

public void test() throws Exception {
assertEquals(
"src/test/pkg/JavaPerformanceTest.java:28: Warning: Avoid object allocations during draw/layout operations (preallocate and reuse instead) [DrawAllocation]\n" +
"        new String(\"foo\");\n" +
//Synthetic comment -- @@ -66,6 +67,12 @@
"src/test/pkg/JavaPerformanceTest.java:190: Warning: Use new SparseIntArray(...) instead for better performance [UseSparseArrays]\n" +
"        new SparseArray<Integer>(); // Use SparseIntArray instead\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
"src/test/pkg/JavaPerformanceTest.java:192: Warning: Use new SparseBooleanArray(...) instead for better performance [UseSparseArrays]\n" +
"        new SparseArray<Boolean>(); // Use SparseBooleanArray instead\n" +
"        ~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
//Synthetic comment -- @@ -90,7 +97,8 @@
"src/test/pkg/JavaPerformanceTest.java:150: Warning: Use Double.valueOf(1.0) instead [UseValueOf]\n" +
"        Double d1 = new Double(1.0);\n" +
"                    ~~~~~~~~~~~~~~~\n" +
            "0 errors, 21 warnings\n",

lintProject("src/test/pkg/JavaPerformanceTest.java.txt=>" +
"src/test/pkg/JavaPerformanceTest.java"));







