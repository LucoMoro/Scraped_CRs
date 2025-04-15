/*Move comment to match code

Followup for change "Lazy parsing of zip files for Java resources."

Change-Id:Ic572dff0edda766400ce6cea9fb27344373c73df*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 3b4c100..728639b 100644

//Synthetic comment -- @@ -383,11 +383,6 @@
init = true;

if (zip == null) {
return;
}

//Synthetic comment -- @@ -409,6 +404,11 @@
maybeInit();

if (zipFile == null || zipFile.getEntry(name) == null) {
                /*
                 * Either this element has no zip/jar file (first
                 * clause), or the zip/jar file doesn't have an entry
                 * for the given name (second clause).
                 */
return null;
}








