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
    public ImmediateProposalAdapter(Text control,
                                    IControlContentAdapter controlContentAdapter,
                                    IContentProposalProvider proposalProvider, KeyStroke keyStroke,
                                    char[] autoActivationCharacters) {
        super(control, controlContentAdapter, proposalProvider, keyStroke,
                autoActivationCharacters);

        control.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent event) {
                openIfNecessary();
            }

            @Override
            public void focusLost(FocusEvent event) {
                // Introduce a slight delay for closing the popup
                control.getDisplay().timerExec(200, () -> {
                    if (!control.isFocusControl() && !isPopupFocus(control)) {
                        close();
                    }
                });
            }

            private boolean isPopupFocus(Text control) {
                // Logic to check if the focus is within the popup
                return control.getParent().isFocusControl();
            }
        });

        control.addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {}

            @Override
            public void mouseDown(MouseEvent e) {}

            @Override
            public void mouseUp(MouseEvent e) {
                // Prevent closing the popup on internal clicks
                if (isPopupOpen()) {
                    e.doit = false;
                }
            }
        });
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