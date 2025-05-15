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

public class LogCatMonitorDialog extends TitleAreaDialog {
    private static final String TITLE = "Auto Monitor Logcat";
    private static final String DEFAULT_MESSAGE =
            "Would you like ADT to automatically monitor logcat output for messages " +
            "from applications in the workspace?";

    private boolean mShouldMonitor = true;
    private static final String[] PRIORITY_LEVELS = {"ERROR", "WARNING", "INFO", "DEBUG", "VERBOSE"};
    private String previouslySelectedLevel = PRIORITY_LEVELS[2]; // Default to INFO

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
        levelCombo.setItems(PRIORITY_LEVELS);
        levelCombo.setText(previouslySelectedLevel);
        
        // Update mShouldMonitor based on selection
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = enableButton.getSelection();
            }
        };
        enableButton.addSelectionListener(selectionAdapter);
        disableButton.addSelectionListener(selectionAdapter);
        
        // Add confirmation logic for both buttons
        enableButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                previouslySelectedLevel = levelCombo.getText();
            }
        });
        
        disableButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mShouldMonitor = false;
            }
        });

        // Tooltips for user guidance
        disableButton.setToolTipText("Select this to stop monitoring logcat output.");
        enableButton.setToolTipText("Select this to start monitoring logcat output.");
        levelCombo.setToolTipText("Choose the minimum priority level to log messages.");

        return c;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(TITLE);
    }

    @Override
    protected void okPressed() {
        mShouldMonitor = enableButton.getSelection();
        super.okPressed();
    }

    @Override
    protected void cancelPressed() {
        levelCombo.setText(previouslySelectedLevel);
        super.cancelPressed();
    }
}

//<End of snippet n. 0>