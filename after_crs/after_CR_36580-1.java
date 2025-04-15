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
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
//Synthetic comment -- @@ -51,25 +54,35 @@
// that we can open the dialog up immediately on focus gain to show all available
// alternatives (the default implementation requires at least one keytroke before
// it shows up)
    private class ImmediateProposalAdapter extends ContentProposalAdapter
        implements FocusListener, IContentProposalListener, IContentProposalListener2 {
        private final PropertyTable m_propertyTable;
        public ImmediateProposalAdapter(
                Text control,
IControlContentAdapter controlContentAdapter,
                IContentProposalProvider proposalProvider,
                KeyStroke keyStroke,
                char[] autoActivationCharacters,
                PropertyTable propertyTable) {
super(control, controlContentAdapter, proposalProvider, keyStroke,
autoActivationCharacters);
            m_propertyTable = propertyTable;

// On focus gain, start completing
            control.addFocusListener(this);

            // Listen on popup open and close events, in order to disable
            // focus handling on the textfield during those events.
            // This is necessary since otherwise as soon as the user clicks
            // on the popup with the mouse, the text field loses focus, and
            // then instantly closes the popup -- without the selection being
            // applied. See for example
            //   http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.jface.snippets/
            //      Eclipse%20JFace%20Snippets/org/eclipse/jface/snippets/viewers/
            //      Snippet060TextCellEditorWithContentProposal.java?view=markup
            // for another example of this technique.
            addContentProposalListener((IContentProposalListener) this);
            addContentProposalListener((IContentProposalListener2) this);

/* Triggering on empty is disabled for now: it has the unfortunate side-effect
that it's impossible to enter a blank text field - blank matches everything,
//Synthetic comment -- @@ -99,6 +112,37 @@
}
});
}

        // ---- Implements FocusListener ----

        @Override
        public void focusGained(FocusEvent event) {
            openIfNecessary();
        }

        @Override
        public void focusLost(FocusEvent event) {
        }

        // ---- Implements IContentProposalListener ----

        @Override
        public void proposalAccepted(IContentProposal proposal) {
            closeProposalPopup();
            m_propertyTable.deactivateEditor(true);
        }

        // ---- Implements IContentProposalListener2 ----

        @Override
        public void proposalPopupClosed(ContentProposalAdapter adapter) {
            m_ignoreFocusLost = false;
        }

        @Override
        public void proposalPopupOpened(ContentProposalAdapter adapter) {
            m_ignoreFocusLost = true;
        }
}
// END ADT MODIFICATIONS

//Synthetic comment -- @@ -108,7 +152,10 @@
// create Text
{
m_textControl = new Text(propertyTable, SWT.NONE);

      @SuppressWarnings("unused")
      TextControlActionsManager manager = new TextControlActionsManager(m_textControl);

m_textControl.setEditable(isEditable());

// BEGIN ADT MODIFICATIONS
//Synthetic comment -- @@ -117,7 +164,8 @@
IContentProposalProvider completion = property.getAdapter(IContentProposalProvider.class);
if (completion != null) {
ImmediateProposalAdapter adapter = new ImmediateProposalAdapter(
                    m_textControl, new TextContentAdapter(), completion, null, null,
                    propertyTable);
adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
ILabelProvider labelProvider = property.getAdapter(ILabelProvider.class);







