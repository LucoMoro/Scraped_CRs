/*Fix propertysheet mouse handling

Selecting items from the field completion popup in the propertysheet
did not work.  This turned out to be related to window focus loss on
the associated text field taking down the popup as soon as the mouse
click transferred focus to it.

Change-Id:I8b90e2b3feb2b91a1f548e66bc5294b265aacd11*/
//Synthetic comment -- diff --git a/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/AbstractTextPropertyEditor.java b/propertysheet/src/org/eclipse/wb/internal/core/model/property/editor/AbstractTextPropertyEditor.java
//Synthetic comment -- index 1cf9574..1ee9237 100644

//Synthetic comment -- @@ -12,6 +12,9 @@

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
//Synthetic comment -- @@ -51,25 +54,35 @@
// that we can open the dialog up immediately on focus gain to show all available
// alternatives (the default implementation requires at least one keytroke before
// it shows up)
    private static class ImmediateProposalAdapter extends ContentProposalAdapter {
        public ImmediateProposalAdapter(Text control,
IControlContentAdapter controlContentAdapter,
                IContentProposalProvider proposalProvider, KeyStroke keyStroke,
                char[] autoActivationCharacters) {
super(control, controlContentAdapter, proposalProvider, keyStroke,
autoActivationCharacters);

// On focus gain, start completing
            control.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent event) {
                    openIfNecessary();
                }

                @Override
                public void focusLost(FocusEvent event) {
                }
            });

/* Triggering on empty is disabled for now: it has the unfortunate side-effect
that it's impossible to enter a blank text field - blank matches everything,
//Synthetic comment -- @@ -99,6 +112,37 @@
}
});
}
}
// END ADT MODIFICATIONS

//Synthetic comment -- @@ -108,7 +152,10 @@
// create Text
{
m_textControl = new Text(propertyTable, SWT.NONE);
      new TextControlActionsManager(m_textControl);
m_textControl.setEditable(isEditable());

// BEGIN ADT MODIFICATIONS
//Synthetic comment -- @@ -117,7 +164,8 @@
IContentProposalProvider completion = property.getAdapter(IContentProposalProvider.class);
if (completion != null) {
ImmediateProposalAdapter adapter = new ImmediateProposalAdapter(
                    m_textControl, new TextContentAdapter(), completion, null, null);
adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
ILabelProvider labelProvider = property.getAdapter(ILabelProvider.class);







