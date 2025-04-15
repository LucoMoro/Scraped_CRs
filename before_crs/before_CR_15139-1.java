/*fillWindow's start position must be smaller than getCount value

Change-Id:I3664ae8f6172f02bf6e2472320e79e3bf8683cc0*/
//Synthetic comment -- diff --git a/core/java/android/database/AbstractCursor.java b/core/java/android/database/AbstractCursor.java
//Synthetic comment -- index 038eedf..a5e5e46 100644

//Synthetic comment -- @@ -204,7 +204,7 @@
* @param window
*/
public void fillWindow(int position, CursorWindow window) {
        if (position < 0 || position > getCount()) {
return;
}
window.acquireReference();







