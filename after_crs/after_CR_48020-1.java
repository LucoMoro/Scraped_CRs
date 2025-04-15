/*Fix formatting of a doc comment for ZipEntry.setComment.

Change-Id:I577c960ea0f486d1b1d55a0d31ce2c71e3776426*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipEntry.java b/luni/src/main/java/java/util/zip/ZipEntry.java
//Synthetic comment -- index 4e10642..563f649 100644

//Synthetic comment -- @@ -176,7 +176,7 @@

/**
* Sets the comment for this {@code ZipEntry}.
     * @throws IllegalArgumentException if the comment is >= 64 Ki UTF-8 bytes.
*/
public void setComment(String comment) {
if (comment == null) {







