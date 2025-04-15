/*38994: UI designer's -> "Edit ID" should allow ID removal

Change-Id:I7ca6454f9a698d2bcd35350e6bd324fd01eaf7d7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index a555ef4..3fe89e0 100644

//Synthetic comment -- @@ -207,6 +207,11 @@
node.editXml("Change ID", new PropertySettingNodeHandler(ANDROID_URI,
ATTR_ID, newId));
editedProperty(ATTR_ID);
                        } else if (newId != null) {
                            // Clear
                            node.editXml("Change ID", new PropertySettingNodeHandler(ANDROID_URI,
                                    ATTR_ID, null));
                            editedProperty(ATTR_ID);
} else if (newId == null) {
// Cancelled
break;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index d7a3026..610fe5d 100644

//Synthetic comment -- @@ -188,6 +188,9 @@
weight = mRulesEngine.displayInput("Enter Weight Value:", weight,
null);
if (weight != null) {
                                    if (weight.isEmpty()) {
                                        weight = null; // remove attribute
                                    }
for (INode child : children) {
child.setAttribute(ANDROID_URI,
ATTR_LAYOUT_WEIGHT, weight);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index c5f976f..5162a48 100644

//Synthetic comment -- @@ -116,6 +116,11 @@
* with a few methods they can use to access functionality from this {@link RulesEngine}.
*/
class ClientRulesEngine implements IClientRulesEngine {
    /** The return code from the dialog for the user choosing "Clear" */
    public static final int CLEAR_RETURN_CODE = -5;
    /** The dialog button ID for the user choosing "Clear" */
    private static final int CLEAR_BUTTON_ID = CLEAR_RETURN_CODE;

private final RulesEngine mRulesEngine;
private final String mFqcn;

//Synthetic comment -- @@ -174,8 +179,28 @@
mFqcn,  // title
message,
value == null ? "" : value, //$NON-NLS-1$
                    validator) {
            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                createButton(parent, CLEAR_BUTTON_ID, "Clear", false /*defaultButton*/);
                super.createButtonsForButtonBar(parent);
            }

            @Override
            protected void buttonPressed(int buttonId) {
                super.buttonPressed(buttonId);

                if (buttonId == CLEAR_BUTTON_ID) {
                    assert CLEAR_RETURN_CODE != Window.OK && CLEAR_RETURN_CODE != Window.CANCEL;
                    setReturnCode(CLEAR_RETURN_CODE);
                    close();
                }
            }
        };
        int result = d.open();
        if (result == ResourceChooser.CLEAR_RETURN_CODE) {
            return "";
        } else if (result == Window.OK) {
return d.getValue();
}
return null;







