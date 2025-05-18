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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class LogCatMonitorDialog extends TitleAreaDialog {
    private static final String TITLE = "Auto Monitor Logcat";
    private static final String DEFAULT_MESSAGE =
            "Would you like ADT to automatically monitor logcat output for messages " +
            "from applications in the workspace?";

    private boolean mShouldMonitor = true;
    private String selectedLogLevel = "Verbose"; // Default selection initialized

    public LogCatMonitorDialog(Shell parentShell) {
        super(parentShell);
        loadUserPreferences();
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
        enableButton.setSelection(mShouldMonitor);

        final Combo levelCombo = new Combo(c, SWT.READ_ONLY | SWT.DROP_DOWN);
        levelCombo.setItems(new String[]{"Verbose", "Debug", "Info", "Warn", "Error"});
        levelCombo.select(getDefaultLogLevelIndex());
        levelCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectedLogLevel = levelCombo.getText();
            }
        });

        disableButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = false;
            }
        });

        enableButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = true;
            }
        });

        return c;
    }

    @Override
    protected void okPressed() {
        if (mShouldMonitor && (selectedLogLevel == null || selectedLogLevel.isEmpty())) {
            MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK);
            messageBox.setMessage("Please select a log level before monitoring.");
            messageBox.open();
            return;
        }

        PreferenceStore preferences = PreferenceStore.getInstance();
        preferences.setShouldMonitor(mShouldMonitor);
        preferences.setSelectedLogLevel(selectedLogLevel);
        
        try {
            // Code to enable logcat monitoring
        } catch (SecurityException ex) {
            MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage("Permission denied accessing logcat. Please allow permissions.");
            messageBox.open();
        } catch (Exception ex) {
            MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage("Error accessing the logcat. Please check your setup.");
            messageBox.open();
        }

        super.okPressed();
        saveUserPreferences(); // Save preferences regardless of dialog closure
    }

    private void loadUserPreferences() {
        PreferenceStore preferences = PreferenceStore.getInstance();
        mShouldMonitor = preferences.getShouldMonitor();
        selectedLogLevel = preferences.getSelectedLogLevel() == null ? "Verbose" : preferences.getSelectedLogLevel(); // Ensure a valid log level is set
    }

    private void saveUserPreferences() {
        PreferenceStore preferences = PreferenceStore.getInstance();
        preferences.setShouldMonitor(mShouldMonitor);
        preferences.setSelectedLogLevel(selectedLogLevel);
    }

    private int getDefaultLogLevelIndex() {
        switch (selectedLogLevel) {
            case "Verbose": return 0;
            case "Debug": return 1;
            case "Info": return 2;
            case "Warn": return 3;
            case "Error": return 4;
            default: return 0; // Default to "Verbose"
        }
    }

}

//<End of snippet n. 0>