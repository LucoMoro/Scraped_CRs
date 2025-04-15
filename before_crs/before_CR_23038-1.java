/*Merge 3586fef5 from master. do not merge.

Add method to LayoutLibrary to query layoutlib api level and rev.

Change-Id:Ib34251b690c18167f69a5b389fcabddbf8bb0aa1*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index dd1a3dc..3d1c5db 100644

//Synthetic comment -- @@ -219,6 +219,33 @@
// ------ Layout Lib API proxy

/**
* Returns whether the LayoutLibrary supports a given {@link Capability}.
* @return true if it supports it.
*







