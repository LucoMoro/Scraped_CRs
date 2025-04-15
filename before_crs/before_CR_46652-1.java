/*Lint fix for AOSP unbundled

Change-Id:I306b753319d235b015f44c0927382578805898de*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index eb04231..c29cdf3 100644

//Synthetic comment -- @@ -865,6 +865,10 @@
File apiDir = new File(getAospTop(), "frameworks/base/api" //$NON-NLS-1$
.replace('/', File.separatorChar));
File[] apiFiles = apiDir.listFiles();
int max = 1;
for (File apiFile : apiFiles) {
String name = apiFile.getName();







