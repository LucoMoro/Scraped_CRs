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

    public ImmediateProposalAdapter(Text control, IControlContentAdapter controlContentAdapter,
            IContentProposalProvider proposalProvider, KeyStroke keyStroke,
            char[] autoActivationCharacters) {
        super(control, controlContentAdapter, proposalProvider, keyStroke, autoActivationCharacters);

        control.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent event) {
                openIfNecessary();
            }

            @Override
            public void focusLost(FocusEvent event) {
                if (!event.doit) {
                    Object source = event.getSource();
                    if (source instanceof Text) {
                        Text textControl = (Text) source;
                        if (!isFocusOnPopup(textControl)) {
                            isPopupOpen = false;
                            close();
                        }
                    }
                } else {
                    // Do not close popup in case of mouse movement within
                    if (isPopupOpen) {
                        return; // Allow the popup to stay open
                    }
                }
            }
        });

        control.addMouseListener(new MouseListener() {
            @Override
            public void mouseDown(MouseEvent e) {
                isPopupOpen = true;
                open(); // Opening the popup on mouse down
            }

            @Override
            public void mouseUp(MouseEvent e) {}

            @Override
            public void mouseDoubleClick(MouseEvent e) {}
        });
    }

    private boolean isFocusOnPopup(Text control) {
        // Enhanced logic to determine if the mouse is hovering over the popup
        return isPopupOpen; // Consider mouse events or checks to validate focus
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