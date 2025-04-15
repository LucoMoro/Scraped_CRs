/*Fix Issue 11735: The Delete key does not always work

Add Backspace as a key handler for the logical delete action.
Most Macs have a Delete key that actually corresponds to
a backspace action, so we must handle SWT.BS, not just
SWT.DEL. I didn't make this code Mac-specific since having
both backspace and delete work in the canvas editor to delete
the selection seems useful.

Change-Id:I8b0be10cb46ba8a16126bbc3cc59832414d7993a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 705021e..880d76e 100755

//Synthetic comment -- @@ -71,6 +71,8 @@
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
//Synthetic comment -- @@ -336,6 +338,23 @@
}
});

        addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                // Set up backspace as an alias for the delete action within the canvas.
                // On most Macs there is no delete key - though there IS a key labeled
                // "Delete" and it sends a backspace key code! In short, for Macs we should
                // treat backspace as delete, and it's harmless (and probably useful) to
                // handle backspace for other platforms as well.
                if (e.keyCode == SWT.BS) {
                    mDeleteAction.run();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });

// --- setup drag'n'drop ---
// DND Reference: http://www.eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html

//Synthetic comment -- @@ -1836,7 +1855,7 @@
* Copies the action attributes form the given {@link ActionFactory}'s action to
* our action.
* <p/>
     * {@link ActionFactory} provides access to the standard global actions in Eclipse.
* <p/>
* This allows us to grab the standard labels and icons for the
* global actions such as copy, cut, paste, delete and select-all.







