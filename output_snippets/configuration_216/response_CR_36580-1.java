//<Beginning of snippet n. 0>

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Text;

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
                // Implement logic for focus lost
                if (!control.isFocusControl()) {
                    closePopup();
                }
            }
        });
    }

    // Override the method to handle mouse interactions
    @Override
    public void openIfNecessary() {
        if (shouldOpen()) {
            super.openIfNecessary();
        }
    }

    private boolean shouldOpen() {
        // Implement logic to check if popup should open
        return true;
    }

    private void closePopup() {
        if (isPopupOpen()) {
            closeProposalPopup();
        }
    }

    // Additional method for debounce mechanism could be implemented here
}

// create Text
{
    m_textControl = new Text(propertyTable, SWT.NONE);
    new TextControlActionsManager(m_textControl);
    m_textControl.setEditable(isEditable());

    // Create Proposal Adapter
    IContentProposalProvider completion = property.getAdapter(IContentProposalProvider.class);
    if (completion != null) {
        ImmediateProposalAdapter adapter = new ImmediateProposalAdapter(
                m_textControl, new TextContentAdapter(), completion, null, null);
        adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
        adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
        ILabelProvider labelProvider = property.getAdapter(ILabelProvider.class);
    }
}

//<End of snippet n. 0>