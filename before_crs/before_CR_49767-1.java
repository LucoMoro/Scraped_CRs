/*Unit test fix

Change-Id:Iff5e204c79b3286d1a65c08c2f5ab3dfc0218c48*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/UnusedResourceDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/UnusedResourceDetectorTest.java
//Synthetic comment -- index d668a81..d9d1987 100644

//Synthetic comment -- @@ -160,12 +160,12 @@
// string1 is defined and used in the library project
// string2 is defined in the library project and used in the master project
// string3 is defined in the library project and not used anywhere
           "/TESTROOT/LibraryProject/res/values/strings.xml:7: Warning: The resource R.string.string3 appears to be unused [UnusedResources]\n" +
"    <string name=\"string3\">String 3</string>\n" +
"            ~~~~~~~~~~~~~~\n" +
"0 errors, 1 warnings\n",

           checkLint(Arrays.asList(master, library)));
}

public void testFqcnReference() throws Exception {
//Synthetic comment -- @@ -221,12 +221,12 @@
assertEquals(
// The strings are all referenced in the library project's manifest file
// which in this project is merged in
           "/TESTROOT/LibraryProject/res/values/strings.xml:7: Warning: The resource R.string.string3 appears to be unused [UnusedResources]\n" +
"    <string name=\"string3\">String 3</string>\n" +
"            ~~~~~~~~~~~~~~\n" +
"0 errors, 1 warnings\n",

           checkLint(Arrays.asList(master, library)));
}

public void testLibraryMerging() throws Exception {







