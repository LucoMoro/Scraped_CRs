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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;

public class LogCatMonitorDialog extends TitleAreaDialog {
    private static final String TITLE = "Auto Monitor Logcat";
    private static final String DEFAULT_MESSAGE =
            "Would you like ADT to automatically monitor logcat output for messages " +
            "from applications in the workspace?";

    private boolean mShouldMonitor = true;
    private String selectedLogLevel;

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

        final Combo levelCombo = new Combo(c, SWT.READ_ONLY | SWT.DROP_DOWN);
        levelCombo.setItems(new String[] {"ERROR", "WARNING", "INFO", "DEBUG"});
        levelCombo.select(0); // Set default selection

        enableButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = true;
                selectedLogLevel = levelCombo.getText();
            }
        });

        disableButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = false;
            }
        });

        return c;
    }

    @Override
    protected void okPressed() {
        if (mShouldMonitor && (selectedLogLevel == null || selectedLogLevel.isEmpty())) {
            // Display message to select a log level
            MessageDialog.openWarning(getShell(), "Warning", "Please select a log level before proceeding.");
            return;
        }
        super.okPressed();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(TITLE);
    }
}

//<End of snippet n. 0>