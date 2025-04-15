/*Allow resource files without a filename extension

Change-Id:I6702deeb9de5562a1a7e61473d446cf4a7ed66b2*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceSet.java b/builder/src/main/java/com/android/builder/resources/ResourceSet.java
//Synthetic comment -- index 1e48c45..7a01b0b 100755

//Synthetic comment -- @@ -471,7 +471,9 @@
int pos;// get the resource name based on the filename
String name = file.getName();
pos = name.indexOf('.');
        if (pos >= 0) {
            name = name.substring(0, pos);
        }

Resource item = new Resource(name, type, null);
ResourceFile resourceFile = new ResourceFile(file, item, qualifiers);







