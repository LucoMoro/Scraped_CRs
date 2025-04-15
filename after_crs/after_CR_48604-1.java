/*Fix typo database location for tests

Change-Id:I3a8b3dfee1aae96b7bb8b4fc6ff4cd551f645306*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index c80d3f8..82ff490 100644

//Synthetic comment -- @@ -388,7 +388,9 @@
String base = relativePath.substring("tools/support/".length());
File rootDir = getRootDir();
if (rootDir != null) {
                    File file = new File(rootDir, "tools"
                            + File.separator + "base"
                            + File.separator + "files"
+ File.separator + "typos"
+ File.separator + base);
return file;







