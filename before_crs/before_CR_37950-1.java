/*Fixed test cases for exported services that previously used wrong test input files.

Change-Id:I8e0f8986da5be6a640a8aadb44f9f7c364c84bce*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SecurityDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SecurityDetectorTest.java
//Synthetic comment -- index 0978826..0264e4d 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
assertEquals(
"AndroidManifest.xml:12: Warning: Exported service does not require permission",
lintProject(
                    "exportservice1.xml=>AndroidManifest.xml",
"res/values/strings.xml"));
}

//Synthetic comment -- @@ -64,7 +64,7 @@
assertEquals(
"No warnings.",
lintProject(
                    "exportservice3.xml=>AndroidManifest.xml",
"res/values/strings.xml"));
}








