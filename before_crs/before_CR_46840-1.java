/*40222: Cannot render strings starting with "\@". DO NOT MERGE

Change-Id:I577c1693aba444077290a03669c71abd07b3b269*/
//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceResolver.java b/sdk_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index d742c4a..756ea53 100644

//Synthetic comment -- @@ -263,6 +263,9 @@

// at this point, value contains type/[android:]name (drawable/foo for instance)
String[] segments = reference.split("\\/");

// now we look for android: in the resource name in order to support format
// such as: @drawable/android:name







