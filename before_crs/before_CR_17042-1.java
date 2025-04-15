/*GLE2: Don't change the selection on right click > menu.

Change-Id:I94c4a1bc57287d250963456339aa24e6a10a92c0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e9708f2..4de76f9 100755

//Synthetic comment -- @@ -1059,6 +1059,14 @@
* pointed at (i.e. click on an object then alt-click to cycle).
*/
private void onMouseUp(MouseEvent e) {
boolean isShift = (e.stateMask & SWT.SHIFT) != 0;
boolean isAlt   = (e.stateMask & SWT.ALT)   != 0;








