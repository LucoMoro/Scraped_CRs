/*Fix DocletRunner to use updated TF path

Change-Id:I8bd781dd3a4e89412faab1a953b8de9ded3182b3*/
//Synthetic comment -- diff --git a/tools/cts-java-scanner/src/com/android/cts/javascanner/DocletRunner.java b/tools/cts-java-scanner/src/com/android/cts/javascanner/DocletRunner.java
//Synthetic comment -- index 8a371fd..995408a 100644

//Synthetic comment -- @@ -75,7 +75,7 @@

private String getClassPath() {
List<String> classPath = new ArrayList<String>();
        classPath.add("./prebuilt/common/tradefed/tradefed-prebuilt.jar");
return join(classPath, ":");
}








