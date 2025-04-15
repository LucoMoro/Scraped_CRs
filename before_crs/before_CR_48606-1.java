/*Make lint tests find resources from $ANDROID_HOME

This is useful when running tests in a context that does not
have a full repo checkout such as a Jenkins server.

Change-Id:Ib7c1a7a090e8373d5e5b90b32f03919b25aca0e0*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 82ff490..264924b 100644

//Synthetic comment -- @@ -378,13 +378,22 @@
@Override
public File findResource(String relativePath) {
if (relativePath.equals("platform-tools/api/api-versions.xml")) {
File rootDir = getRootDir();
if (rootDir != null) {
File file = new File(rootDir, "development" + File.separator + "sdk"
+ File.separator + "api-versions.xml");
                    return file;
}
} else if (relativePath.startsWith("tools/support/")) {
String base = relativePath.substring("tools/support/".length());
File rootDir = getRootDir();
if (rootDir != null) {
//Synthetic comment -- @@ -393,7 +402,14 @@
+ File.separator + "files"
+ File.separator + "typos"
+ File.separator + base);
                    return file;
}
} else {
fail("Unit tests don't support arbitrary resource lookup yet.");








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 9afef42..60d0f22 100644

//Synthetic comment -- @@ -304,7 +304,10 @@
path = System.getenv(PROP_BIN_DIR);
}
if (path != null && !path.isEmpty()) {
            return new File(path);
}
return null;
}







