//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

Label placeholder = new Label(mPackageRootComposite, SWT.NONE);
placeholder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

mLicenseRadioAcceptAll = new Button(mPackageRootComposite, SWT.RADIO);
mLicenseRadioAcceptAll.setText("Accept All");
return sLastSize;
} else {
    return new Point(800, 600); // Adjusted dialog height
}

// New button for copying package text to clipboard
Button copyButton = new Button(mPackageRootComposite, SWT.PUSH);
copyButton.setText("Copy to Clipboard");
copyButton.addListener(SWT.Selection, event -> {
    if (packageText != null && !packageText.isEmpty()) {
        Clipboard clipboard = new Clipboard(display);
        TextTransfer textTransfer = TextTransfer.getInstance();
        try {
            clipboard.setContents(new Object[]{packageText}, new Transfer[]{textTransfer});
        } catch (SWTError e) {
            MessageBox messageBox = new MessageBox(mPackageRootComposite.getShell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage("Failed to copy to clipboard: " + e.getMessage());
            messageBox.open();
        } finally {
            clipboard.dispose();
        }
    } else {
        MessageBox messageBox = new MessageBox(mPackageRootComposite.getShell(), SWT.ICON_INFORMATION | SWT.OK);
        messageBox.setMessage("No text to copy.");
        messageBox.open();
    }
});

// New button for printing package information
Button printButton = new Button(mPackageRootComposite, SWT.PUSH);
printButton.setText("Print");
printButton.addListener(SWT.Selection, event -> {
    MessageBox confirmBox = new MessageBox(mPackageRootComposite.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
    confirmBox.setMessage("Do you want to print the package information?");
    if (confirmBox.open() == SWT.YES) {
        Printer printer = new Printer();
        try {
            if (printer.startJob("Package Print Job")) {
                printer.startPage();
                printer.drawText(packageText, 10, 10);
                printer.endPage();
                printer.endJob();
            }
        } catch (SWTError e) {
            MessageBox messageBox = new MessageBox(mPackageRootComposite.getShell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage("Printing failed: " + e.getMessage());
            messageBox.open();
        } finally {
            printer.dispose();
        }
    }
});

//<End of snippet n. 0>