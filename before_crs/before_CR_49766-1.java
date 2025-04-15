/*Sort file listing results

This CL sorts the XML files encountered by lint and the
manifest merger unit tests; this should ensure that we
have stable test output

Change-Id:Ied5ff5964710d4393eada70cced4afd583b6bcb1*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index dc62b5d..c21e283 100644

//Synthetic comment -- @@ -1564,6 +1564,9 @@
if (xmlFiles != null && xmlFiles.length > 0) {
XmlVisitor visitor = getVisitor(type, checks);
if (visitor != null) { // if not, there are no applicable rules in this folder
for (File file : xmlFiles) {
if (LintUtils.isXmlFile(file)) {
XmlContext context = new XmlContext(this, project, main, file, type);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index f960118..eaa6597 100644

//Synthetic comment -- @@ -283,6 +283,7 @@
if (folder.getName().startsWith(typeName)) {
File[] files = folder.listFiles();
if (files != null) {
for (File file : files) {
String fileName = file.getName();
if (fileName.startsWith(name)








//Synthetic comment -- diff --git a/manifmerger/src/test/java/com/android/manifmerger/ManifestMergerTestCase.java b/manifmerger/src/test/java/com/android/manifmerger/ManifestMergerTestCase.java
//Synthetic comment -- index 29378f0..52576ac 100755

//Synthetic comment -- @@ -32,11 +32,11 @@
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.TestCase;

//Synthetic comment -- @@ -367,6 +367,8 @@
assert mainFile != null;
assert actualResultFile != null;

return new TestFiles(
shouldFail,
mainFile,







