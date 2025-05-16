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

        control.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent event) {
                openIfNecessary();
            }

            @Override
            public void focusLost(FocusEvent event) {
                if (!control.isDisposed()) {
                    // Prevent popup from closing if mouse is over the control
                    if (!control.getBounds().contains(control.toDisplay(event.x, event.y))) {
                        close();
                    }
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