/*Add workaround for the Eclipse+Mountain Lion bug

On Mountain Lion, the property sheet's property editor inline text
editor is broken. This is due tohttps://bugs.eclipse.org/bugs/show_bug.cgi?id=388574The bug has been fixed for Eclipse 4.3, but right now it's a very poor
user experience; you click to edit, and nothing happens.

This changeset adds a workaround: *If* the bug is triggered (an
exception is thrown during property editor activation), then the
customizer dialog is shown instead (equivalent to clicking on the
"..." button next to the property).

The dialog code is modified to show an error message on the bottom
explaining why the dialog was shown instead (since this behavior is
annoying, we should explain to users why we are doing), and it
explains that the bug should be fixed in Eclipse 4.3, so if anyone in
the future is running Eclipse 4.2 with this behavior and 4.3 is
available, they will have an incentive to upgrade.

(This is the property editor side of the workaround; the ADT side is
in the SDK git repository)

Change-Id:Id618183a04f4402bed046664cbf98ee6c764ef1f*/




//Synthetic comment -- diff --git a/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/presentation/ButtonPropertyEditorPresentation.java b/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/presentation/ButtonPropertyEditorPresentation.java
//Synthetic comment -- index e33970d..892681e 100644

//Synthetic comment -- @@ -111,4 +111,15 @@
* Handles click on {@link Button}.
*/
protected abstract void onClick(PropertyTable propertyTable, Property property) throws Exception;

  // Temporary workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=388574
  public static boolean isInWorkaround;
  public void click(PropertyTable propertyTable, Property property) throws Exception {
    try {
      isInWorkaround = true;
      onClick(propertyTable, property);
    } finally {
        isInWorkaround = false;
    }
  }
}








//Synthetic comment -- diff --git a/propertysheet/src/org/eclipse/wb/internal/core/model/property/table/PropertyTable.java b/propertysheet/src/org/eclipse/wb/internal/core/model/property/table/PropertyTable.java
//Synthetic comment -- index 7a49cb3..20233e9 100644

//Synthetic comment -- @@ -38,12 +38,14 @@
import org.eclipse.wb.draw2d.IColorConstants;
import org.eclipse.wb.draw2d.ICursorConstants;
import org.eclipse.wb.internal.core.DesignerPlugin;
import org.eclipse.wb.internal.core.EnvironmentUtils;
import org.eclipse.wb.internal.core.model.property.Property;
import org.eclipse.wb.internal.core.model.property.category.PropertyCategory;
import org.eclipse.wb.internal.core.model.property.category.PropertyCategoryProvider;
import org.eclipse.wb.internal.core.model.property.category.PropertyCategoryProviders;
import org.eclipse.wb.internal.core.model.property.editor.PropertyEditor;
import org.eclipse.wb.internal.core.model.property.editor.complex.IComplexPropertyEditor;
import org.eclipse.wb.internal.core.model.property.editor.presentation.ButtonPropertyEditorPresentation;
import org.eclipse.wb.internal.core.model.property.editor.presentation.PropertyEditorPresentation;
import org.eclipse.wb.internal.core.utils.check.Assert;
import org.eclipse.wb.internal.core.utils.ui.DrawUtils;
//Synthetic comment -- @@ -558,6 +560,24 @@
}
// set bounds
setActiveEditorBounds();
    } catch (NullPointerException e) {
        if (EnvironmentUtils.IS_MAC) {
            // Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=388574
            PropertyEditor editor = property.getEditor();
            PropertyEditorPresentation presentation = editor.getPresentation();
            if (presentation instanceof ButtonPropertyEditorPresentation) {
                ButtonPropertyEditorPresentation button =
                        (ButtonPropertyEditorPresentation) presentation;
                try {
                    button.click(this, property);
                } catch (Exception ex) {
                    deactivateEditor(false);
                    handleException(e);
                }
                return;
            }
        }
        DesignerPlugin.log(e);
} catch (Throwable e) {
DesignerPlugin.log(e);
}







