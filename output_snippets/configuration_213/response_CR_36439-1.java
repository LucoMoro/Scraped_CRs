//<Beginning of snippet n. 0>

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.GridLayout;

public class LogCatMonitorDialog extends TitleAreaDialog {
    private static final String TITLE = "Auto Monitor Logcat";
    private static final String DEFAULT_MESSAGE =
            "Would you like ADT to automatically monitor logcat output for messages " +
            "from applications in the workspace?";

    private boolean mShouldMonitor = true;
    private Combo levelCombo;

    public LogCatMonitorDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite shell) {
        setTitle(TITLE);
        setMessage(DEFAULT_MESSAGE);

        Composite parent = (Composite) super.createDialogArea(shell);
        Composite c = new Composite(parent, SWT.BORDER);
        c.setLayout(new GridLayout(2, false));
        c.setLayoutData(new GridData(GridData.FILL_BOTH));

        final Button disableButton = new Button(c, SWT.RADIO);
        disableButton.setText("No, do not monitor logcat output.");
        GridDataFactory.defaultsFor(disableButton).span(2, 1).applyTo(disableButton);

        final Button enableButton = new Button(c, SWT.RADIO);
        enableButton.setText("Yes, monitor logcat and display logcat view if there are " +
                             "messages with priority higher than:");
        enableButton.setSelection(true);

        levelCombo = new Combo(c, SWT.READ_ONLY | SWT.DROP_DOWN);
        levelCombo.setItems(new String[] {"VERBOSE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL"});
        GridDataFactory.defaultsFor(levelCombo).span(2, 1).applyTo(levelCombo);

        enableButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = true;
            }
        });

        disableButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = false;
            }
        });

        return c;
    }

    @Override
    protected void okPressed() {
        if (levelCombo.getSelectionIndex() == -1) {
            // Display error: No priority selected
            return;
        }
        super.okPressed();
    }
    
    @Override
    protected void cancelPressed() {
        // Handle cancel logic if needed
        super.cancelPressed();
    }
}

//<End of snippet n. 0>