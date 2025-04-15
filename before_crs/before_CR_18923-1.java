/*Fix Build: DragSourceEvent has no offsetX/Y in SWT 3.4

Change-Id:I0edf5ed0d6bcfaa8de679203a2bb1c9beb67cc12*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index e44b0b3..7bf336c 100755

//Synthetic comment -- @@ -62,6 +62,7 @@
import org.w3c.dom.Element;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -603,8 +604,20 @@
if (!mIsPlaceholder) {
// Shift the drag feedback image up such that it's centered under the
// mouse pointer
                event.offsetX = mImage.getBounds().width / 2;
                event.offsetY = mImage.getBounds().height / 2;
// ...and record this info in the drag state object such that we can
// account for it when performing the drop, since we want to place the newly
// inserted object where it is currently shown, not with its top left corner







