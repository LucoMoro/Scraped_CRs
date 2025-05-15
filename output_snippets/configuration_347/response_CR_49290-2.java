//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;

public class PackageDialog {

    private Control mPackageRootComposite;
    private Button mLicenseRadioAcceptAll;

    public Point getDialogSize() {
        // Use preferred sizes rather than arbitrary values
        return new Point(740, 500); // Adjusted height for better usability
    }

    private void copyToClipboard(String text) {
        try {
            Clipboard clipboard = new Clipboard(Display.getDefault());
            TextTransfer textTransfer = TextTransfer.getInstance();
            clipboard.setContents(new Object[]{text}, new Transfer[]{textTransfer});
            MessageBox messageBox = new MessageBox(mPackageRootComposite.getShell(), SWT.ICON_INFORMATION | SWT.OK);
            messageBox.setMessage("Text copied to clipboard successfully.");
            messageBox.open();
        } catch (Exception e) {
            MessageBox messageBox = new MessageBox(mPackageRootComposite.getShell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage("Failed to copy text to clipboard.");
            messageBox.open();
        }
    }

    private void printPackageInfo() {
        try {
            PrintJob job = new PrintJob("Print Package Info");
            job.run();
            // Assume print logic is handled within the PrintJob class
        } catch (Exception e) {
            MessageBox messageBox = new MessageBox(mPackageRootComposite.getShell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage("Failed to print package information.");
            messageBox.open();
        }
    }
}

//<End of snippet n. 0>