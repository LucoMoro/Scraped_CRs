//<Beginning of snippet n. 0>

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Text;

// that we can open the dialog up immediately on focus gain to show all available
// alternatives (the default implementation requires at least one key stroke before
// it shows up)
private static class ImmediateProposalAdapter extends ContentProposalAdapter {
    public ImmediateProposalAdapter(Text control,
                                    IControlContentAdapter controlContentAdapter,
                                    IContentProposalProvider proposalProvider, KeyStroke keyStroke,
                                    char[] autoActivationCharacters) {
        super(control, controlContentAdapter, proposalProvider, keyStroke, autoActivationCharacters);
        
        // On focus gain, start completing
        control.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent event) {
                openIfNecessary();
            }

            @Override
            public void focusLost(FocusEvent event) {
                if (event.getSource() instanceof Text) {
                    Text source = (Text) event.getSource();
                    // Prevent popup closure if focus moves to the popup items
                    if (!isMovingFocusToPopupItems(source)) {
                        close();
                    }
                }
            }
        });

        /* Triggering on empty is disabled for now: it has the unfortunate side-effect
           that it's impossible to enter a blank text field - blank matches everything,
        }
    }

    private boolean isMovingFocusToPopupItems(Text source) {
        // Implement logic to check if focus is moving to a popup item
        return false; // Placeholder: replace with actual implementation that checks for popup items
    }
}

// create Text
{
    m_textControl = new Text(propertyTable, SWT.NONE);
    new TextControlActionsManager(m_textControl);
    m_textControl.setEditable(isEditable());

    // BEGIN ADT MODIFICATIONS
    IContentProposalProvider completion = property.getAdapter(IContentProposalProvider.class);
    if (completion != null) {
        ImmediateProposalAdapter adapter = new ImmediateProposalAdapter(
                m_textControl, new TextContentAdapter(), completion, null, null);
        adapter.setFilterStyle(ContentProposalAdapter.FILTER_NONE);
        adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
        ILabelProvider labelProvider = property.getAdapter(ILabelProvider.class);
    }
//<End of snippet n. 0>