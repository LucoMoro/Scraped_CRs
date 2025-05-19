//<Beginning of snippet n. 0>


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Clipboard;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.PrintDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.graphics.TextBounds;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TextTransfer;

//... existing code ...

Label placeholder = new Label(mPackageRootComposite, SWT.NONE);
placeholder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

mLicenseRadioAcceptAll = new Button(mPackageRootComposite, SWT.RADIO);
mLicenseRadioAcceptAll.setText("Accept All");

Button copyButton = new Button(mPackageRootComposite, SWT.PUSH);
copyButton.setText("Copy");
copyButton.addListener(SWT.Selection, event -> {
    Clipboard clipboard = new Clipboard(Display.getCurrent());
    try {
        Control[] selectedControls = mPackageRootComposite.getChildren(); 
        StringBuilder selectedText = new StringBuilder();
        for (Control control : selectedControls) {
            if (control instanceof Text) {
                String text = ((Text) control).getText();
                if (!text.isEmpty() && ((Text) control).getSelectionCount() > 0) {
                    selectedText.append(text.substring(((Text) control).getSelection().x, ((Text) control).getSelection().y));
                    selectedText.append("\n");
                }
            }
        }
        if (selectedText.length() > 0) {
            clipboard.setContents(new String[] { selectedText.toString().trim() }, new Transfer[] { TextTransfer.getInstance() });
        } else {
            // Provide user-friendly message
            System.err.println("No text selected to copy.");
        }
    } catch (Exception e) {
        System.err.println("Error copying text: " + e.getMessage());
    } finally {
        clipboard.dispose();
    }
});

Button printButton = new Button(mPackageRootComposite, SWT.PUSH);
printButton.setText("Print");
printButton.addListener(SWT.Selection, event -> {
    PrintDialog printDialog = new PrintDialog(shell);
    PrinterData printerData = printDialog.open();
    if (printerData != null) {
        Printer printer = new Printer(printerData);
        try {
            printer.open();
            GC gc = new GC(printer);
            StringBuilder printContent = new StringBuilder("Package Information:\n");
            for (Control control : mPackageRootComposite.getChildren()) {
                if (control instanceof Text) {
                    String text = ((Text) control).getText();
                    if (!text.isEmpty()) {
                        printContent.append(text).append("\n");
                    }
                }
            }
            gc.drawText(printContent.toString(), 10, 10);
            gc.dispose();
            printer.end();
        } catch (Exception e) {
            System.err.println("Error during printing: " + e.getMessage());
        } finally {
            printer.dispose();
        }
    } else {
        System.err.println("Print operation was cancelled.");
    }
});

return new Point(740, 450); // Increased dialog height


//<End of snippet n. 0>