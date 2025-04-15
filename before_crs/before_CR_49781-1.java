/*Faster ResourceFolderType.getFolderType(String folderName). DO NOT MERGE

Change-Id:Ib7ecb6531fca16fad27701bce68c27e46021dd95*/
//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/resources/ResourceFolderType.java b/layoutlib_api/src/main/java/com/android/resources/ResourceFolderType.java
//Synthetic comment -- index 65c1cb3..5a271cf 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
* @return the enum or null if not found.
*/
public static ResourceFolderType getTypeByName(String name) {
for (ResourceFolderType rType : values()) {
if (rType.mName.equals(name)) {
return rType;
//Synthetic comment -- @@ -67,10 +68,10 @@
* <code>null</code> if no matching type was found.
*/
public static ResourceFolderType getFolderType(String folderName) {
        // split the name of the folder in segments.
        String[] folderSegments = folderName.split(ResourceConstants.RES_QUALIFIER_SEP);

        // get the enum for the resource type.
        return getTypeByName(folderSegments[0]);
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/test/java/com/android/resources/ResourceFolderTypeTest.java b/layoutlib_api/src/test/java/com/android/resources/ResourceFolderTypeTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a3c2c51

//Synthetic comment -- @@ -0,0 +1,30 @@







