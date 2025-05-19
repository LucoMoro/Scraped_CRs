//<Beginning of snippet n. 0>

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Text;

private static class ImmediateProposalAdapter extends ContentProposalAdapter {
    private boolean isPopupOpen = false;
    private Text control;

    public ImmediateProposalAdapter(Text control,
            IControlContentAdapter controlContentAdapter,
            IContentProposalProvider proposalProvider, KeyStroke keyStroke,
            char[] autoActivationCharacters) {
        super(control, controlContentAdapter, proposalProvider, keyStroke,
                autoActivationCharacters);
        this.control = control;

        control.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent event) {
                openIfNecessary();
            }

            @Override
            public void focusLost(FocusEvent event) {
                if (!isFocusOnPopup() && !control.isFocusControl()) {
                    closePopup();
                }
            }
        });

        control.addMouseListener(new MouseListener() {
            @Override
            public void mouseDown(MouseEvent e) {
                isPopupOpen = true; 
            }

            @Override
            public void mouseUp(MouseEvent e) {
                if (!isFocusOnPopup() && !control.isFocusControl()) {
                    isPopupOpen = false; 
                }
                openIfNecessary();
            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });

        control.addMouseTrackListener(new MouseTrackAdapter() {
            @Override
            public void mouseEnter(MouseEvent e) {
                isPopupOpen = true;
            }

            @Override
            public void mouseExit(MouseEvent e) {
                isPopupOpen = false;
            }
        });
    }

    @Override
    protected void openIfNecessary() {
        if (isPopupOpen || !control.isFocusControl()) {
            return;
        }
        super.openIfNecessary();
        isPopupOpen = true;
    }

    private void closePopup() {
        isPopupOpen = false;
    }

    private boolean isFocusOnPopup() {
        // Logic to check if mouse is over the popup or its items
        // Actual implementation needed
        return false; // Replace with actual implementation
    }
}

// create Text
{
    m_textControl = new Text(propertyTable, SWT.NONE);
    new TextControlActionsManager(m_textControl);
    m_textControl.setEditable(isEditable());

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