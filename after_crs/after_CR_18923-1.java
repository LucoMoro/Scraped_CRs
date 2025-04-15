/*Fix Build: DragSourceEvent has no offsetX/Y in SWT 3.4

Change-Id:I0edf5ed0d6bcfaa8de679203a2bb1c9beb67cc12*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index e44b0b3..7bf336c 100755

//Synthetic comment -- @@ -62,6 +62,7 @@
import org.w3c.dom.Element;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//Synthetic comment -- @@ -603,8 +604,20 @@
if (!mIsPlaceholder) {
// Shift the drag feedback image up such that it's centered under the
// mouse pointer

                // TODO quick'ndirty fix. swt.dnd in 3.4 doesn't have offsetX/Y.
                try {
                    Field xField = event.getClass().getDeclaredField("offsetX");
                    Field yField = event.getClass().getDeclaredField("offsetY");

                    xField.set(event, Integer.valueOf(mImage.getBounds().width / 2));
                    yField.set(event, Integer.valueOf(mImage.getBounds().height / 2));
                } catch (SecurityException e) {
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }

// ...and record this info in the drag state object such that we can
// account for it when performing the drop, since we want to place the newly
// inserted object where it is currently shown, not with its top left corner







