/*Fix NullException in QwertyKeyListener.KeyDown

Extending null protection for view to "c == i" case in addition to
"c == Character.toUpperCase(i)" case that helped to fix some force closures.
Seems as typo in original implementation.

Change-Id:Ibaa5aea95c5c8cdc8ac981fe0d1dad313e3e692fSigned-off-by: Dmitry Tsyganyuk <fdt017@motorola.com>*/




//Synthetic comment -- diff --git a/core/java/android/text/method/QwertyKeyListener.java b/core/java/android/text/method/QwertyKeyListener.java
//Synthetic comment -- index c5261f3..162731f 100644

//Synthetic comment -- @@ -115,7 +115,7 @@
if (count > 0 && selStart == selEnd && selStart > 0) {
char c = content.charAt(selStart - 1);

                if ((c == i || c == Character.toUpperCase(i)) && view != null) {
if (showCharacterPicker(view, content, c, false, count)) {
resetMetaState(content);
return true;







