//<Beginning of snippet n. 0>

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class LogCatMonitorDialog extends TitleAreaDialog {
    private static final String TITLE = "Auto Monitor Logcat";
    private static final String DEFAULT_MESSAGE =
            "Would you like ADT to automatically monitor logcat output for messages " +
            "from applications in the workspace?";
    
    private static final String[] LOG_LEVELS = {"VERBOSE", "DEBUG", "INFO", "WARN", "ERROR"};

    private boolean mShouldMonitor = true;
    private String selectedLogLevel;

    public LogCatMonitorDialog(Shell parentShell) {
        super(parentShell);
        selectedLogLevel = LOG_LEVELS[2]; // Default log level (INFO)
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
        levelCombo.setItems(LOG_LEVELS);
        levelCombo.select(2);  // Default selection set to INFO
        
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

        levelCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectedLogLevel = levelCombo.getItem(levelCombo.getSelectionIndex());
            }
        });

        return c;
    }

    @Override
    protected void okPressed() {
        if (mShouldMonitor && !isValidLogLevel(selectedLogLevel)) {
            setErrorMessage("Invalid selection: Please select a valid log level.");
            return;
        }
        savePreferences();
        super.okPressed();
    }

    private boolean isValidLogLevel(String logLevel) {
        for (String level : LOG_LEVELS) {
            if (level.equals(logLevel)) {
                return true;
            }
        }
        return false;
    }

    private void savePreferences() {
        // Logic to save user preferences for monitoring state and log level
    }

    @Override
    public void close() {
        super.close();
    }

    public boolean isMonitoringEnabled() {
        return mShouldMonitor;
    }

    public String getSelectedLogLevel() {
        return selectedLogLevel;
    }
}

//<End of snippet n. 0>