/*SDK Manager: make "choose package to install" dialog taller.

Also add an option to copy the package text to clipboard
or to print (using the system's default printer, whatever
that is.)

SDK Bug: 42445

Change-Id:I6969951b8bf1cc58f65fe05b2e5f16ecd2c2e64f*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 49bebba..42737ce 100755

//Synthetic comment -- @@ -52,6 +52,7 @@
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
//Synthetic comment -- @@ -211,8 +212,29 @@
}
});

        //--Label placeholder = new Label(mPackageRootComposite, SWT.NONE);
        //--placeholder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

        Link link = new Link(mPackageRootComposite, SWT.NONE);
        link.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        final String printAction = "Print"; // extracted for NLS, to compare with below.
        link.setText(String.format("<a>Copy to clipboard</a> | <a>%1$s</a>", printAction));
        link.setToolTipText("Copies all text and license to clipboard | Print using system defaults.");
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                if (printAction.equals(e.text)) {
                    mPackageText.print();
                } else {
                    Point p = mPackageText.getSelection();
                    mPackageText.selectAll();
                    mPackageText.copy();
                    mPackageText.setSelection(p);
                }
            }
        });


mLicenseRadioAcceptAll = new Button(mPackageRootComposite, SWT.RADIO);
mLicenseRadioAcceptAll.setText("Accept All");
//Synthetic comment -- @@ -366,7 +388,7 @@
return sLastSize;
} else {
// Arbitrary values that look good on my screen and fit on 800x600
            return new Point(740, 470);
}
}








