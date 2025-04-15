/*Prevent beeps in property editor

Clicking focus into a property editor which both has
proposal support as well as has a current value which
is not a proposal match (for example, a TextView's text
property can have a hardcoded string which is not a
match for the proposals which are for @-resources) would
cause an audible beep. This CL prevents that.

Change-Id:I0a006f594266cce248b91d065f4d438659b1743b*/
//Synthetic comment -- diff --git a/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/AbstractTextPropertyEditor.java b/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/AbstractTextPropertyEditor.java
//Synthetic comment -- index 1ee9237..dd651af 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
private class ImmediateProposalAdapter extends ContentProposalAdapter
implements FocusListener, IContentProposalListener, IContentProposalListener2 {
private final PropertyTable m_propertyTable;
public ImmediateProposalAdapter(
Text control,
IControlContentAdapter controlContentAdapter,
//Synthetic comment -- @@ -67,6 +68,7 @@
super(control, controlContentAdapter, proposalProvider, keyStroke,
autoActivationCharacters);
m_propertyTable = propertyTable;

// On focus gain, start completing
control.addFocusListener(this);
//Synthetic comment -- @@ -103,6 +105,12 @@
}

private void openIfNecessary() {
getControl().getDisplay().asyncExec(new Runnable() {
@Override
public void run() {







