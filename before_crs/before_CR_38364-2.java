/*Updated refernces to Notepad Sample files

Copy and Paste reference code in NotePad sample has been moved
to different files hence changing their refernces in ClipData
documentation

Please note for this patch to build you need a related patchI38c6460e0ab60616da699ebfdfa1996f2a9deef0Change-Id:I05f39b974a5d7512780f3b6978ebe09c58425d93*/
//Synthetic comment -- diff --git a/core/java/android/content/ClipData.java b/core/java/android/content/ClipData.java
//Synthetic comment -- index a8b1bf4..3f6147c 100644

//Synthetic comment -- @@ -86,7 +86,7 @@
* the entire structure of the note into a new note; otherwise, it simply
* coerces the clip into text and uses that as the new note's contents.
*
 * {@sample development/samples/NotePad/src/com/example/android/notepad/NoteEditor.java
*      paste}
*
* <p>In many cases an application can paste various types of streams of data.  For
//Synthetic comment -- @@ -134,7 +134,7 @@
* <p>The copy operation in our NotePad application is now just a simple matter
* of making a clip containing the URI of the note being copied:
*
 * {@sample development/samples/NotePad/src/com/example/android/notepad/NotesList.java
*      copy}
*
* <p>Note if a paste operation needs this clip as text (for example to paste







