//<Beginning of snippet n. 0>

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Text;

private static class ImmediateProposalAdapter extends ContentProposalAdapter {
    public ImmediateProposalAdapter(Text control,
                                    IControlContentAdapter controlContentAdapter,
                                    IContentProposalProvider proposalProvider, KeyStroke keyStroke,
                                    char[] autoActivationCharacters) {
        super(control, controlContentAdapter, proposalProvider, keyStroke, autoActivationCharacters);

        // On focus gain, start completing
        control.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                openIfNecessary();
            }

            @Override
            public void focusLost(FocusEvent event) {
                // Handle focus lost logic here to determine if the popup should remain open
                // Implement your logic to check if the new focus is within the popup
            }
        });

        // Prevent closing the popup when interacting with items
        control.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                // Capture mouse events to prevent focus transfer back to the text field
                // logic to ensure that clicking on items does not close the popup
            }
        });
        
        /* Triggering on empty is disabled for now: it has the unfortunate side-effect
           that it's impossible to enter a blank text field - blank matches everything,
        }
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
}
//<End of snippet n. 0>