/*do not package .scala files as resources

Change-Id:I8b0ac703fd60517f8c7857e7f7ce316e08ab51be*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index c066dff..3904743 100644

//Synthetic comment -- @@ -881,6 +881,7 @@
"rsh".equalsIgnoreCase(extension) == false &&           // RenderScript header files
"d".equalsIgnoreCase(extension) == false &&             // Dependency files
"java".equalsIgnoreCase(extension) == false &&          // Java files
"class".equalsIgnoreCase(extension) == false &&         // Java class files
"scc".equalsIgnoreCase(extension) == false &&           // VisualSourceSafe
"swp".equalsIgnoreCase(extension) == false &&           // vi swap file







